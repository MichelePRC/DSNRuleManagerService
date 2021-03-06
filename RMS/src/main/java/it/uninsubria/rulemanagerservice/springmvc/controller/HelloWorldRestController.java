package it.uninsubria.rulemanagerservice.springmvc.controller;

import java.util.Base64;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mysql.jdbc.util.Base64Decoder;

import it.uninsubria.rulemanagerservice.springmvc.controller.AesUtil;
import it.uninsubria.rulemanagerservice.springmvc.model.Resource;
import it.uninsubria.rulemanagerservice.springmvc.model.UploadRequest;
import it.uninsubria.rulemanagerservice.springmvc.model.User;
import it.uninsubria.rulemanagerservice.springmvc.service.ResourceService;
import it.uninsubria.rulemanagerservice.springmvc.service.UploadRequestService;
import it.uninsubria.rulemanagerservice.springmvc.service.UserService;
 
@Controller
@RestController
public class HelloWorldRestController  {
	
	private PrivateKey privKey;
	
    @Autowired
    UserService userService;  
	
    @Autowired
    ResourceService resourceService;
    
    @Autowired
    UploadRequestService uploadRequestService;
    
	@Autowired
	MessageSource messageSource;
	
	
	@Value("${my.modulus}")
	private BigInteger modulus;
	
	@Value("${my.public.exponent}")
	private BigInteger publicExponent;
	
	@Value("${my.private.exponent}")
	private BigInteger privateExponent;
	
	
	@Value("${KMS.modulus}")
	private BigInteger KMSmodulus;
	
	@Value("${KMS.public.exponent}")
	private BigInteger KMSpublicExponent;
  
	
	@Value("${PFS.modulus}")
	private BigInteger PFSmodulus;
	
	@Value("${PFS.public.exponent}")
	private BigInteger PFSpublicExponent;
	
	
		
	
	
	//1 CREATEUSER
	//DA FARE: LA CREAZIONE COMPRENDE CREAZ USER (IDU E SECRET) LATO RMS, INVIO DEL MESSAGGIO A KMS, RICEZIONE CHIAVI CLIENT (CIFRATE) INVIO CHIAVI CIFRATE A CLIENT
	@RequestMapping(value = "/createSocialUser1/", method = RequestMethod.POST)
    public void createUser1 (HttpServletRequest request, HttpServletResponse response ) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JSONException {

		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        
        JSONObject msgtoRMS=new JSONObject(sb.toString());
        
        int iduser=msgtoRMS.getInt("idu");
        String userSecret=new AesUtil(128,1000).random(128/8).toString();
        
        User user=new User();
        user.setIdu(iduser);
        user.setSecret(userSecret);
        System.out.println(user.toString());
        userService.saveUser(user);
        
        String msgtoKMS=msgtoRMS.getString("encryptedmsgtoKMS");
        

        URL url = new URL("http://193.206.170.148/KMS/clientAsymmKeyPairReq/");
		URLConnection urlConnection = url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		urlConnection.connect();
		OutputStream outputStream = urlConnection.getOutputStream();
		outputStream.write(msgtoKMS.getBytes());		
		outputStream.flush();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

		String line;
	    StringBuffer responseKMS = new StringBuffer(); 
	    while((line = reader.readLine()) != null) {
	    	responseKMS.append(line);
	    	responseKMS.append('\r');
	    }
		
	    PrintWriter pw = null;
    	
  	 	try{
       	pw = response.getWriter();    	
       	pw.println(responseKMS.toString());
       	}catch(Exception ex)
       	{
       	pw.println("{");
       	pw.println("\"successful\": false,");
       	pw.println("\"message\": \""+ex.getMessage()+"\",");
       	pw.println("}");
       	return;
       	}
		
		
	}
    
    //2 CREATEUSER: RICEVE DAL CLIENT LA PUBLIC KEY E LA MEMORIZZA NELLA SUA TABELLA USER
	@RequestMapping(value = "/createSocialUser2/", method = RequestMethod.POST)
    public void createUser2 (HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JSONException {
	
		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject messaggioInChiaro = new JSONObject(sb.toString());
        
        String paramRMS=messaggioInChiaro.getString("encrypted_symmKey");			//ACCEDO ALLA KEYSIMM CIFRATA TRAMITE CHIAVE PUBBLICA DI RMS
        
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PrivateKey priv = factory.generatePrivate(spec);

    	Cipher cipher;
        
        byte[] dectyptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          
          byte[] messaggioCifratoBytes = new byte[256];

          BigInteger messaggioCifrato = new BigInteger(paramRMS.toString(), 16);
          if (messaggioCifrato.toByteArray().length > 256) {
              for (int i=1; i<257; i++) {
            	  messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
              }
          } else {
        	  messaggioCifratoBytes = messaggioCifrato.toByteArray();
          }
         
          cipher.init(Cipher.DECRYPT_MODE, priv);
          dectyptedText = cipher.doFinal(messaggioCifratoBytes);
          } catch(NoSuchAlgorithmException e) {
        	  System.out.println(e);
          } catch(NoSuchPaddingException e) { 
        	  System.out.println(e);
          } catch(InvalidKeyException e) {
        	  System.out.println(e);
          } catch(IllegalBlockSizeException e) {
        	  System.out.println(e);
          } catch(BadPaddingException e) {
        	  System.out.println(e);
          }
          String messaggioDecifrato = new String(dectyptedText);
          JSONObject AESSimmKey = new JSONObject(messaggioDecifrato);
          
          System.out.println(messaggioDecifrato);
          
          String salt=AESSimmKey.getString("salt");							//ACCEDO ALLA KEYSIMM in CHIARO
          String iv=AESSimmKey.getString("iv");
          String passphrase=AESSimmKey.getString("passPhrase");
          
          AesUtil aesUtil=new AesUtil(128, 1000);
          String strmsgRMS=aesUtil.decrypt(salt, iv, passphrase, messaggioInChiaro.getString("clientPubKeyToRMS"));		//OTTENGO LA STRINGA DEL MESSAGGIO A RMS
          
          JSONObject clientPubKeyToRMS=new JSONObject(strmsgRMS);	
        
        
        User user=userService.findByIdu(clientPubKeyToRMS.getInt("idu"));
        user.setModulus((new BigInteger(clientPubKeyToRMS.getString("client_mod"), 16)).toString());
        user.setExponent((new BigInteger(clientPubKeyToRMS.getString("client_pub_exp"), 16)).toString());
        userService.updateUser(user);
	}
	
    
    @RequestMapping(value = "/getPublicKeys/", method = RequestMethod.GET)
    public void getPublicKeys( HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{
    
		 //genero la coppia di chiavi asimmetriche di RMS
		 /*KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		 kpg.initialize(2048);
		 KeyPair kp = kpg.genKeyPair();
		 PublicKey pubKey=kp.getPublic();
		 
		 this.privKey=kp.getPrivate();
	
	     //genero le specifiche della sua chiave pubblica per accedere a modulus e publicExponent   	
	     //KeyFactory fact = KeyFactory.getInstance("RSA");
	   	 //RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),RSAPublicKeySpec.class);
	   	 	     
	     KeyFactory fact;
	     RSAPublicKeySpec pub = new RSAPublicKeySpec(BigInteger.ZERO, BigInteger.ZERO);
	     try {
	         fact = KeyFactory.getInstance("RSA");
	         pub = fact.getKeySpec(pubKey,    RSAPublicKeySpec.class);
	     } catch(NoSuchAlgorithmException e1) {
	     } catch(InvalidKeySpecException e) {
	     }*/
	   	 
    	 
	   	 
	   	 String modulo=modulus.toString(16);
	   	 String esponentePubblico=publicExponent.toString(16);
	     
    	
	   	 JSONObject chiave = new JSONObject();
	   	 chiave.put("modulo", modulo);
	   	 chiave.put("esponente_pubblico", esponentePubblico);
   	 	 PrintWriter pw = null;
    	
   	 	 try{
        	pw = response.getWriter();    	
        	pw.println(chiave);
        	}catch(Exception ex)
        	{
        	pw.println("{");
        	pw.println("\"successful\": false,");
        	pw.println("\"message\": \""+ex.getMessage()+"\",");
        	pw.println("}");
        	return;
        	}
	   	 
	   	 
	   	 
    }
    
    

    @RequestMapping(value = "/decryptRequest/", method = RequestMethod.POST)
    public void decryptReq ( HttpServletRequest request,  HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{
    
    	StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
            	
        Cipher cipher;
        BigInteger messaggioCifrato = new BigInteger(sb.toString(), 16);
        byte[] dectyptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          byte[] messaggioCifratoBytes = messaggioCifrato.toByteArray();
          cipher.init(Cipher.DECRYPT_MODE, privKey);
          dectyptedText = cipher.doFinal(messaggioCifratoBytes);
          } catch(NoSuchAlgorithmException e) {
          } catch(NoSuchPaddingException e) { 
          } catch(InvalidKeyException e) {
          } catch(IllegalBlockSizeException e) {
          } catch(BadPaddingException e) {
          }
          String messaggioDecifrato = new String(dectyptedText);
          JSONObject messaggio = new JSONObject(messaggioDecifrato);
          System.out.println(messaggio);
    	
          
          
        PrintWriter pw = null;
      	
      	try{
          	pw = response.getWriter();    	 	
        	pw.println(messaggio);
        	}catch(Exception ex)
          	{
          	pw.println("{");
          	pw.println("\"successful\": false,");
          	pw.println("\"message\": \""+ex.getMessage()+"\",");
          	pw.println("}");
          	return;
          	}
    }
    
    
    
    @RequestMapping(value = "/encryptRequest/", method = RequestMethod.GET)
    public void encryptReq (HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{
    
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger("21871005673239196546630580968462831307780469444887567939897040269323277972115493387540984165984578291334061693954186883497564820760294114586601170284492964146139601341591907492065262191508078944716985902273080075977250863395468651386500153820020784187286356376422405683468337530184935533499369993400065440663615072138870687957222077094903187094492955893981881798107102483194997547801580022125313577909988540108152634392752342472418061358322613613405745500147071774348964100102811374966359763933478950255285021671818822221975496075767203934308154253723625295088346417846682528316030599387281961857135797862333405488417"), new BigInteger("65537"));    	
    	PublicKey pub = factory.generatePublic(spec);
    	
    	RSAPrivateKeySpec spec2 = new RSAPrivateKeySpec(new BigInteger("21871005673239196546630580968462831307780469444887567939897040269323277972115493387540984165984578291334061693954186883497564820760294114586601170284492964146139601341591907492065262191508078944716985902273080075977250863395468651386500153820020784187286356376422405683468337530184935533499369993400065440663615072138870687957222077094903187094492955893981881798107102483194997547801580022125313577909988540108152634392752342472418061358322613613405745500147071774348964100102811374966359763933478950255285021671818822221975496075767203934308154253723625295088346417846682528316030599387281961857135797862333405488417"), new BigInteger("12055300195921277080892978117777001055316259185301715126735137139158553083978517221287677373270471736517564805108580603009994836277902018635677261323481755906055311638672292998510238970444754013747293875729934917139969943532629042566579665785654375512787163552525794328541285456160651406261062325733310404481767461235364506711100606082332405978183913371074562925601118083106626924146272455899400281706938176681595505490176073476471203085419686996863290921094783918658451732117578867201727953770866436242646497771895231699344955343433269397332526102377825741135550383455556271315284756734869344298644794253301728984113"));
    	PrivateKey priv = factory.generatePrivate(spec2);
    	
    	
    	
    	String text="funzionaaaaa";    	
        Cipher cipher;

        byte[] encryptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          byte[] messaggioDaCifrare =text.getBytes();
          cipher.init(Cipher.ENCRYPT_MODE, pub);
          encryptedText = cipher.doFinal(messaggioDaCifrare);
          } catch(NoSuchAlgorithmException e) {
          } catch(NoSuchPaddingException e) { 
          } catch(InvalidKeyException e) {
          } catch(IllegalBlockSizeException e) {
          } catch(BadPaddingException e) {
          }
          String encrypted=new String(Base64.getEncoder().encode(encryptedText));
          String messaggioCifrato = byteArrayToHexString(encryptedText);

        byte[] ciphertextBytes = Base64.getDecoder().decode(encrypted.getBytes());
  		byte[] decryptedText = new byte[1];
          try {
            cipher = javax.crypto.Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priv);
            decryptedText = cipher.doFinal(ciphertextBytes);
            } catch(NoSuchAlgorithmException e) {
            } catch(NoSuchPaddingException e) { 
            } catch(InvalidKeyException e) {
            } catch(IllegalBlockSizeException e) {
            } catch(BadPaddingException e) {
            }
          String decryptedString = new String(decryptedText);
          System.out.println(new String(decryptedString));
          
          
        PrintWriter pw = null;
      	
      	try{
          	pw = response.getWriter();    	 	
        	pw.println(messaggioCifrato);
        	}catch(Exception ex)
          	{
          	pw.println("{");
          	pw.println("\"successful\": false,");
          	pw.println("\"message\": \""+ex.getMessage()+"\",");
          	pw.println("}");
          	return;
          	}
    }
    
    
    @RequestMapping(value = "/clientKeys/", method = RequestMethod.POST)
    public void clientKeys (HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{
    
    	RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PrivateKey priv = factory.generatePrivate(spec);
    	
    	
    	StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
            	
        Cipher cipher;
                       
        byte[] dectyptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          
          byte[] messaggioCifratoBytes = new byte[256];

          BigInteger messaggioCifrato = new BigInteger(sb.toString(), 16);
          if (messaggioCifrato.toByteArray().length > 256) {
              for (int i=1; i<257; i++) {
            	  messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
              }
          } else {
        	  messaggioCifratoBytes = messaggioCifrato.toByteArray();
          }
         
          cipher.init(Cipher.DECRYPT_MODE, priv);
          dectyptedText = cipher.doFinal(messaggioCifratoBytes);
          } catch(NoSuchAlgorithmException e) {
        	  System.out.println(e);
          } catch(NoSuchPaddingException e) { 
        	  System.out.println(e);
          } catch(InvalidKeyException e) {
        	  System.out.println(e);
          } catch(IllegalBlockSizeException e) {
        	  System.out.println(e);
          } catch(BadPaddingException e) {
        	  System.out.println(e);
          }
          String messaggioDecifrato = new String(dectyptedText);
          JSONObject messaggio = new JSONObject(messaggioDecifrato);
          System.out.println(messaggio);
    	
        
        String iv =messaggio.getString("iv");
        String salt = messaggio.getString("salt");
        String passphrase = messaggio.getString("passPhrase");
        int iterationCount = messaggio.getInt("iterationCount");
        int keySize = messaggio.getInt("keySize");
       
                
    	
    	//KMS genera la coppia di chiavi (client) le invia a RMS che le inoltra al client (messaggio cifrato
    	//tramite chiave simmetrica e algoritmo AES)
    	
    	//KMS
		 KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		 kpg.initialize(2048);
		 KeyPair kp = kpg.genKeyPair();
		 PublicKey clientPublicKey =kp.getPublic();
		 
		 PrivateKey clientPrivateKey=kp.getPrivate();
		 
	
	     //genero le specifiche della chiave pubblica per accedere a modulus e publicExponent   		   	 	     
	     KeyFactory fact;
	     RSAPublicKeySpec clientPub = new RSAPublicKeySpec(BigInteger.ZERO, BigInteger.ZERO);
	     try {
	         fact = KeyFactory.getInstance("RSA");
	         clientPub = fact.getKeySpec(clientPublicKey,    RSAPublicKeySpec.class);
	     } catch(NoSuchAlgorithmException e1) {
	     } catch(InvalidKeySpecException e) {
	     }
	     
	   //genero le specifiche della chiave privata per accedere a privateExponent
	     RSAPrivateKeySpec clientPriv = new RSAPrivateKeySpec(BigInteger.ZERO, BigInteger.ZERO);
	     try {
	         fact = KeyFactory.getInstance("RSA");
	         clientPriv = fact.getKeySpec(clientPrivateKey,    RSAPrivateKeySpec.class);
	     } catch(NoSuchAlgorithmException e1) {
	     } catch(InvalidKeySpecException e) {
	     }
	     
	     String clientModulus= clientPub.getModulus().toString(16);
	   	 String clientPublicExponent=clientPub.getPublicExponent().toString(16);
	   	 String clientPrivateExponent=clientPriv.getPrivateExponent().toString(16);
	   	 System.out.println(clientPrivateExponent.getBytes().length);
	   	 
	   	JSONObject jsonmsg = new JSONObject();
	   	jsonmsg.put("client_modulus", clientModulus);
	   	jsonmsg.put("client_public_exponent", clientPublicExponent);
	   	jsonmsg.put("client_private_exponent", clientPrivateExponent);
	   	
	   	AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String encrypted_client_keys = aesUtil.encrypt(salt, iv, passphrase, jsonmsg.toString());
        
        ///////////////////////////////////////////FINE INVIO DELLE CHIAVI CLIENT TRAMITE MESSAGGIO CIFRATO CON AES /////////////////////////////////////////////////////////////
        //AGGIUNTA CIFRATURA TRAMITE RSA 
        KeyFactory factory2 = KeyFactory.getInstance("RSA");
    	RSAPublicKeySpec spec2a = new RSAPublicKeySpec(new BigInteger("21871005673239196546630580968462831307780469444887567939897040269323277972115493387540984165984578291334061693954186883497564820760294114586601170284492964146139601341591907492065262191508078944716985902273080075977250863395468651386500153820020784187286356376422405683468337530184935533499369993400065440663615072138870687957222077094903187094492955893981881798107102483194997547801580022125313577909988540108152634392752342472418061358322613613405745500147071774348964100102811374966359763933478950255285021671818822221975496075767203934308154253723625295088346417846682528316030599387281961857135797862333405488417"), new BigInteger("65537"));    	
    	PublicKey pub2 = factory2.generatePublic(spec2a);
    	
    	RSAPrivateKeySpec spec2b = new RSAPrivateKeySpec(new BigInteger("21871005673239196546630580968462831307780469444887567939897040269323277972115493387540984165984578291334061693954186883497564820760294114586601170284492964146139601341591907492065262191508078944716985902273080075977250863395468651386500153820020784187286356376422405683468337530184935533499369993400065440663615072138870687957222077094903187094492955893981881798107102483194997547801580022125313577909988540108152634392752342472418061358322613613405745500147071774348964100102811374966359763933478950255285021671818822221975496075767203934308154253723625295088346417846682528316030599387281961857135797862333405488417"), new BigInteger("12055300195921277080892978117777001055316259185301715126735137139158553083978517221287677373270471736517564805108580603009994836277902018635677261323481755906055311638672292998510238970444754013747293875729934917139969943532629042566579665785654375512787163552525794328541285456160651406261062325733310404481767461235364506711100606082332405978183913371074562925601118083106626924146272455899400281706938176681595505490176073476471203085419686996863290921094783918658451732117578867201727953770866436242646497771895231699344955343433269397332526102377825741135550383455556271315284756734869344298644794253301728984113"));
    	PrivateKey priv2 = factory2.generatePrivate(spec2b);
    	
    	JSONObject jsonmsg2 = new JSONObject();
	   	jsonmsg2.put("encrypted_client_keys", encrypted_client_keys);
	   	jsonmsg2.put("funziona", "s�");

	   	String doppiacifratura=jsonmsg2.toString();    	
        

	   	String iv2=random(16);
	   	String salt2=random(16);
	   	String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
	   	String passphrase2=RandomStringUtils.random(16, characters);
        
        String AESencryptedClientKeys = aesUtil.encrypt(salt2, iv2, passphrase2, doppiacifratura);
        
        JSONObject jsonmsg3= new JSONObject();
        jsonmsg3.put("iv2", iv2);
        jsonmsg3.put("salt2", salt2);
        jsonmsg3.put("passphrase2", passphrase2);
        String RSAEncryptedSimmKey=jsonmsg3.toString();
        	   	
        
        JSONObject jsonmsg4 = new JSONObject();
	   	jsonmsg4.put("AESencryptedClientKeys", AESencryptedClientKeys);
        
	   	
	   	Cipher cipher2;
        byte[] encryptedText = new byte[1];
        try {
          cipher2 = javax.crypto.Cipher.getInstance("RSA");
          byte[] messaggioDaCifrare =RSAEncryptedSimmKey.getBytes();
          cipher2.init(Cipher.ENCRYPT_MODE, pub2);
          encryptedText = cipher2.doFinal(messaggioDaCifrare);
          //encryptedText = HelloWorldRestController.blockCipher(messaggioDaCifrare,Cipher.ENCRYPT_MODE, cipher2);
          } catch(NoSuchAlgorithmException e) {
          } catch(NoSuchPaddingException e) { 
          } catch(InvalidKeyException e) {
          } catch(IllegalBlockSizeException e) {
          } catch(BadPaddingException e) {
          }
          String messaggioCifrato = byteArrayToHexString(encryptedText);
       
          jsonmsg4.put("RSAEncryptedSimmKey", messaggioCifrato);
	    
        PrintWriter pw = null;
        try{
          	pw = response.getWriter();    	 	
        	pw.println(jsonmsg4);
        	}catch(Exception ex)
          	{
          	pw.println("{");
          	pw.println("\"successful\": false,");
          	pw.println("\"message\": \""+ex.getMessage()+"\",");
          	pw.println("}");
          	return;
          	} 
    	
    	
    }
    
    
    @RequestMapping(value = "/uploadReq1/", method = RequestMethod.POST)
    public void uploadReq1 (HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{



    	StringBuilder sb = new StringBuilder();
    	BufferedReader br = request.getReader();
    	String str = null;
    	while ((str = br.readLine()) != null) {
    		sb.append(str);
    	}
    	JSONObject messaggioInChiaro = new JSONObject(sb.toString());

    	String paramRMS=messaggioInChiaro.getString("paramRMS");			//ACCEDO ALLA KEYSIMM CIFRATA TRAMITE CHIAVE PUBBLICA DI RMS

    	RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PrivateKey priv = factory.generatePrivate(spec);

    	Cipher cipher;

    	byte[] dectyptedText = new byte[1];
    	try {
    		cipher = javax.crypto.Cipher.getInstance("RSA");

    		byte[] messaggioCifratoBytes = new byte[256];

    		BigInteger messaggioCifrato = new BigInteger(paramRMS.toString(), 16);
    		if (messaggioCifrato.toByteArray().length > 256) {
    			for (int i=1; i<257; i++) {
    				messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
    			}
    		} else {
    			messaggioCifratoBytes = messaggioCifrato.toByteArray();
    		}

    		cipher.init(Cipher.DECRYPT_MODE, priv);
    		dectyptedText = cipher.doFinal(messaggioCifratoBytes);
    	} catch(NoSuchAlgorithmException e) {
    		System.out.println(e);
    	} catch(NoSuchPaddingException e) { 
    		System.out.println(e);
    	} catch(InvalidKeyException e) {
    		System.out.println(e);
    	} catch(IllegalBlockSizeException e) {
    		System.out.println(e);
    	} catch(BadPaddingException e) {
    		System.out.println(e);
    	}
    	String messaggioDecifrato = new String(dectyptedText);
    	JSONObject AESSimmKey = new JSONObject(messaggioDecifrato);

    	String salt=AESSimmKey.getString("salt");							//ACCEDO ALLA KEYSIMM in CHIARO
    	String iv=AESSimmKey.getString("iv");
    	String passphrase=AESSimmKey.getString("passPhrase");

    	AesUtil aesUtil=new AesUtil(128, 1000);
    	String strmsgRMS=aesUtil.decrypt(salt, iv, passphrase, messaggioInChiaro.getString("encryptmsgRMS"));		//OTTENGO LA STRINGA DEL MESSAGGIO A RMS

    	JSONObject jsonmsgRMS=new JSONObject(strmsgRMS);							//MSG IN JSON A RMS

    	int n1=jsonmsgRMS.getInt("n1");
    	int idu=jsonmsgRMS.getInt("idu");
    	int idresource=jsonmsgRMS.getInt("idR");
    	String token=jsonmsgRMS.getString("session_token");


    	Resource rsc=new Resource();																
    	User user=userService.findByIdu(idu);
    	PrintWriter pw = null;


    	rsc.setIdR(idresource);
    	rsc.setOwner(user);
    	resourceService.saveResource(rsc);

    	UploadRequest uprequest=new UploadRequest();								//SALVO LA RICHIESTA DI UPLOAD PER IL NONCE N1 E CONTROLLO SUCCESSIVO NEL CONTROLLER PATH 2 UPLOAD
    	uprequest.setToken(token);
    	uprequest.setNonce(n1);
    	uprequest.setUtente(user);

    	//uploadRequestService.save(uprequest);


    	String msgKMS=jsonmsgRMS.getString("msgKMS");								//PRENDO IL MSG DIRETTO A KMS E LO INVIO A KMS

    	URL url = new URL("http://193.206.170.148/KMS/uploadReq1/");

    	URLConnection urlConnection = url.openConnection();
    	urlConnection.setDoOutput(true);
    	urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
    	urlConnection.connect();
    	OutputStream outputStream = urlConnection.getOutputStream();
    	outputStream.write(msgKMS.getBytes());		
    	outputStream.flush();

    	BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


    	StringBuffer responseFromKMS = new StringBuffer(); 
    	String line;
    	while((line = reader.readLine()) != null) {
    		responseFromKMS.append(line);
    		responseFromKMS.append('\r');
    	}

    	JSONObject msgToClient=new JSONObject();								//RICEVO LA RISPOSTA DA KMS DIRETTA AL CLIENT

    	msgToClient.put("secretUser", user.getSecret());
    	msgToClient.put("nonce_one_plus_one", n1+1);
    	msgToClient.put("KMSmsg", responseFromKMS.toString());				//CREO IL MSG DIRETTO AL CLIENT CONTENENTE LA RISPOSTA DI RMS E IL MSG DI KMS


    	String iv2=random(16);							//CIFRO TUTTO TRAMITE CHIAVE PUBBLICA DEL CLIENT
    	String salt2=random(16);
    	String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
    	String passphrase2=RandomStringUtils.random(16, characters);

    	JSONObject AESKeyParams=new JSONObject();
    	AESKeyParams.put("iv", iv2);
    	AESKeyParams.put("salt", salt2);
    	AESKeyParams.put("passphrase", passphrase2);

    	factory = KeyFactory.getInstance("RSA");
    	RSAPublicKeySpec spec2 = new RSAPublicKeySpec(new BigInteger(userService.findByIdu(idu).getModulus()), new BigInteger(userService.findByIdu(idu).getExponent()));    	
    	PublicKey clientPub = factory.generatePublic(spec2);


    	Cipher cipher2;
    	byte[] encryptedText = new byte[1];
    	try {
    		cipher2 = javax.crypto.Cipher.getInstance("RSA");
    		byte[] messaggioDaCifrare =AESKeyParams.toString().getBytes();
    		cipher2.init(Cipher.ENCRYPT_MODE, clientPub);
    		encryptedText = cipher2.doFinal(messaggioDaCifrare);
    		//encryptedText = HelloWorldRestController.blockCipher(messaggioDaCifrare,Cipher.ENCRYPT_MODE, cipher2);
    	} catch(NoSuchAlgorithmException e) {
    	} catch(NoSuchPaddingException e) { 
    	} catch(InvalidKeyException e) {
    	} catch(IllegalBlockSizeException e) {
    	} catch(BadPaddingException e) {
    	}
    	String AESParams = byteArrayToHexString(encryptedText);

    	AesUtil aesUtil2=new AesUtil(128, 1000);
    	String encrypted_msg_client=aesUtil2.encrypt(salt2, iv2, passphrase2, msgToClient.toString());

    	JSONObject final_msg=new JSONObject();
    	final_msg.put("encrypted_msg_client", encrypted_msg_client);
    	final_msg.put("AESParams", AESParams);        


    	try{
    		pw = response.getWriter();    	 	
    		pw.println(final_msg);
    	}catch(Exception ex)
    	{
    		pw.println("{");
    		pw.println("\"successful\": false,");
    		pw.println("\"message\": \""+ex.getMessage()+"\",");
    		pw.println("}");
    		return;
    	}               	

    }
    
    
    
    @RequestMapping(value = "/uploadReq2/", method = RequestMethod.POST)
    public void uploadReq2 (HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{
    
    	StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject messaggioInChiaro = new JSONObject(sb.toString());
        
        String paramRMS=messaggioInChiaro.getString("keyRMSencrypt");			//ACCEDO ALLA KEYSIMM CIFRATA TRAMITE CHIAVE PUBBLICA DI RMS
        
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PrivateKey priv = factory.generatePrivate(spec);

    	Cipher cipher;
        
        byte[] dectyptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          
          byte[] messaggioCifratoBytes = new byte[256];

          BigInteger messaggioCifrato = new BigInteger(paramRMS.toString(), 16);
          if (messaggioCifrato.toByteArray().length > 256) {
              for (int i=1; i<257; i++) {
            	  messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
              }
          } else {
        	  messaggioCifratoBytes = messaggioCifrato.toByteArray();
          }
         
          cipher.init(Cipher.DECRYPT_MODE, priv);
          dectyptedText = cipher.doFinal(messaggioCifratoBytes);
          } catch(NoSuchAlgorithmException e) {
        	  System.out.println(e);
          } catch(NoSuchPaddingException e) { 
        	  System.out.println(e);
          } catch(InvalidKeyException e) {
        	  System.out.println(e);
          } catch(IllegalBlockSizeException e) {
        	  System.out.println(e);
          } catch(BadPaddingException e) {
        	  System.out.println(e);
          }
          String messaggioDecifrato = new String(dectyptedText);
          JSONObject AESSimmKey = new JSONObject(messaggioDecifrato);
    	
          String salt=AESSimmKey.getString("salt");							//ACCEDO ALLA KEYSIMM in CHIARO
          String iv=AESSimmKey.getString("iv");
          String passphrase=AESSimmKey.getString("passPhrase");
          
          /*File dir = new File("/usr/share/tomcat7/apache-tomcat-7.0.69/webapps");
  	   	  dir.mkdirs();
  	   	  File tmp = new File(dir, "debug.txt");
  	   	  tmp.createNewFile();
    	
  	   	  PrintWriter writer = new PrintWriter("/usr/share/tomcat7/apache-tomcat-7.0.69/webapps/debug.txt", "UTF-8");
  	   	  writer.println("SALT: "+salt);
  	   	  writer.println("IV: "+iv);
  	   	  writer.println("PASSPHRASE: "+passphrase);
  	   	  writer.println("MSGRMS: "+messaggioInChiaro.getString("msgRMS"));
  	   	  
  	   	  writer.close();         
          
  	   	  System.out.println(messaggioInChiaro.getString("msgRMS"));
  	   	  System.out.println(salt);
  	   	  System.out.println(iv);
  	   	  System.out.println(passphrase);*/
  	   	  
          AesUtil aesUtil=new AesUtil(128, 1000);
          String strmsgRMS=aesUtil.decrypt(salt, iv, passphrase, messaggioInChiaro.getString("msgRMS"));			//OTTENGO LA STRINGA DEL MESSAGGIO A RMS
          
          JSONObject jsonmsgRMS=new JSONObject(strmsgRMS);							//MSG IN JSON A RMS
          
          
          /* PARTE DI CODICE IN CUI FACCIO IL CONTROLLO DEL NONCE N1: N1+2 CERCANDO LA RIGA NELLA TABELLA UPLOADREQUEST
          jsonmsgRMS.get(key)*/
                   
          
          int nonce_one_plus_2=jsonmsgRMS.getInt("N1_2");
          
          
          int idu=jsonmsgRMS.getInt("id");
          int idResource=jsonmsgRMS.getInt("idResource");
          int ruleRsc=jsonmsgRMS.getInt("rule");
          
          Resource rsc=resourceService.findByIdR(idResource);
          rsc.setSharingDepth(ruleRsc);
          resourceService.update(rsc);
          
          
      	  String msgKMS=jsonmsgRMS.getString("messageToKMS");								//PRENDO IL MSG DIRETTO A KMS E LO INVIO A KMS
          
          URL url = new URL("http://193.206.170.148/KMS/uploadReq2/");
  		
  		  URLConnection urlConnection = url.openConnection();
  		  urlConnection.setDoOutput(true);
  		  urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
  		  urlConnection.connect();
  		  OutputStream outputStream = urlConnection.getOutputStream();
  		  outputStream.write(msgKMS.getBytes());		
  		  outputStream.flush();

  		  BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


  	      StringBuffer responseFromKMS = new StringBuffer(); 
  	      String line;
  	      while((line = reader.readLine()) != null) {
  	    	responseFromKMS.append(line);
  	    	responseFromKMS.append('\r');
  	      }
          
  	      JSONObject msgToClient=new JSONObject();			
          
          
    	
          
    	
    	
    }
    
    
    @RequestMapping(value = "/downloadReq/", method = RequestMethod.POST)
    public void downloadReq(HttpServletRequest request, HttpServletResponse response ) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JSONException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }	
		
		RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, privateExponent);			
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PrivateKey priv = factory.generatePrivate(spec);
        
         Cipher cipher=null;
		 byte [] dectyptedText = new byte[1];
         try {
           cipher = javax.crypto.Cipher.getInstance("RSA");
           
           byte[] messaggioCifratoBytes = new byte[256];

           BigInteger messaggioCifrato = new BigInteger(sb.toString(), 16);
           if (messaggioCifrato.toByteArray().length > 256) {
               for (int i=1; i<257; i++) {
             	  messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
               }
           } else {
         	  messaggioCifratoBytes = messaggioCifrato.toByteArray();
           }
          
           cipher.init(Cipher.DECRYPT_MODE, priv);
           dectyptedText = cipher.doFinal(messaggioCifratoBytes);
           } catch(NoSuchAlgorithmException e) {
         	  System.out.println(e);
           } catch(NoSuchPaddingException e) { 
         	  System.out.println(e);
           } catch(InvalidKeyException e) {
         	  System.out.println(e);
           } catch(IllegalBlockSizeException e) {
         	  System.out.println(e);
           } catch(BadPaddingException e) {
         	  System.out.println(e);
           }
           String messaggioDecifrato = new String(dectyptedText);
           JSONObject jsonmsg = new JSONObject(messaggioDecifrato);

           int idRequestor=jsonmsg.getInt("idRequestor");
           int idResource=jsonmsg.getInt("idResource");
           int nonce_one=jsonmsg.getInt("N1");
           
           Resource rsc=resourceService.findByIdR(idResource);
           
           User owner=rsc.getOwner();
           int idOwner=owner.getIdu();
           int ruleRsc=rsc.getSharingDepth();
           
           JSONObject jsonmsgKMS=new JSONObject();					//GENERA IL MSG PI� INTERNO DIRETTO A KMS E LO CIFRA CON LA CHIAVE PUBBLICA DI KMS
           jsonmsgKMS.put("idRequestor", idRequestor);
           jsonmsgKMS.put("idResource", idResource);
           
           
           RSAPublicKeySpec KMSspec = new RSAPublicKeySpec(KMSmodulus, KMSpublicExponent);
       	   KeyFactory KMSfactory = KeyFactory.getInstance("RSA");
       	   PublicKey KMSpub = KMSfactory.generatePublic(KMSspec);
       	      	
           Cipher cipher2;

           byte[] encryptedText = new byte[1];
           try {
             cipher2 = javax.crypto.Cipher.getInstance("RSA");
             byte[] messaggioDaCifrare =jsonmsgKMS.toString().getBytes();
             cipher2.init(Cipher.ENCRYPT_MODE, KMSpub);
             encryptedText = cipher2.doFinal(messaggioDaCifrare);
             } catch(NoSuchAlgorithmException e) {
             } catch(NoSuchPaddingException e) { 
             } catch(InvalidKeyException e) {
             } catch(IllegalBlockSizeException e) {
             } catch(BadPaddingException e) {
             }
           
           String encryptedmsgKMS = new String(Base64.getEncoder().encode(encryptedText));			//MSG DESTINATO A KMS CIFRATO TRAMITE CHIAVE PUBBLICA KMS
           
           JSONObject jsonmsgPFS=new JSONObject();
           
           jsonmsgPFS.put("idRequestor", idRequestor);
           jsonmsgPFS.put("idOwner", idOwner);
           jsonmsgPFS.put("ruleRsc", ruleRsc);
           
           jsonmsgPFS.put("msgtoKMS", encryptedmsgKMS);  
           
           RSAPublicKeySpec PFSspec = new RSAPublicKeySpec(PFSmodulus, PFSpublicExponent);
       	   KeyFactory PFSfactory = KeyFactory.getInstance("RSA");
       	   PublicKey PFSpub = PFSfactory.generatePublic(PFSspec);
           
           byte [] plaintext=jsonmsgPFS.toString().getBytes();
  		   Cipher cipher3 = Cipher.getInstance("RSA");
  		   cipher3.init(Cipher.ENCRYPT_MODE, PFSpub);
  		   byte [] encryptedmsgPFS=blockCipher(plaintext,Cipher.ENCRYPT_MODE, cipher3);			//MSG (CONTENENTE MSG DIRETTO A KMS) DESTINATO A PFS, CIFRATO TRAMITE CHIAVE PUBBLICA PFS 
           
  		   char[] encryptedTranspherable = Hex.encodeHex(encryptedmsgPFS);
  		   String strmsgencrypted=new String(encryptedTranspherable);
  		   
           
  		   URL url = new URL("http://193.206.170.147/PathFinder/evaluationRequest/");						//INVIA IL MSG A PFS CON RICHIESTA DI EVALUATION
   		
  		   URLConnection urlConnection = url.openConnection();
  		   urlConnection.setDoOutput(true);
  		   urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
  		   urlConnection.connect();
  		   OutputStream outputStream = urlConnection.getOutputStream();
  		   outputStream.write(strmsgencrypted.getBytes());		
  		   outputStream.flush();
           
           
           // PFS FA LA VALUTAZIONE, LA INVIA A KMS CHE RISPONDE A RMS CON LINK DI DOWNLOAD DEL FILE E TOKEN (==SECRET_RSC / RANDOM)
  		   
  		   BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
  		   

 	       StringBuffer responseFromKMS = new StringBuffer(); 
 	       String line;
 	       while((line = reader.readLine()) != null) {
 	    	 responseFromKMS.append(line);
 	    	 responseFromKMS.append('\r');
 	       }
           
 	       
           String secret_owner=owner.getSecret();
           int nonce_one_plus_one=nonce_one + 1;
           
           JSONObject msgtoClient=new JSONObject();
           msgtoClient.put("secret_owner", secret_owner);
           msgtoClient.put("N1_1", nonce_one_plus_one);
           msgtoClient.put("msgFromKMS", responseFromKMS.toString());
           
           
          String iv=random(16);							//CIFRO TUTTO TRAMITE CHIAVE PUBBLICA DEL CLIENT (REQUESTOR)
   	   	  String salt=random(16);
   	   	  String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
   	   	  String passphrase=RandomStringUtils.random(16, characters);
   	   	  
   	   	  JSONObject AESKeyParams=new JSONObject();
   	   	  AESKeyParams.put("iv", iv);
   	   	  AESKeyParams.put("salt", salt);
   	   	  AESKeyParams.put("passphrase", passphrase);
   	   	    	   	  
   	   	  User requestor=userService.findByIdu(idRequestor);
   	   	  
   	   	  factory = KeyFactory.getInstance("RSA");
   	   	  RSAPublicKeySpec spec2 = new RSAPublicKeySpec(new BigInteger(requestor.getModulus()), new BigInteger(requestor.getExponent()));    	
     	  PublicKey clientPub = factory.generatePublic(spec2);  	   	  

   	   	  byte[] encryptedText2 = new byte[1];
   	   	  try {
   	   		  cipher3 = javax.crypto.Cipher.getInstance("RSA");
   	   		  byte[] messaggioDaCifrare =AESKeyParams.toString().getBytes();
   	   		  cipher3.init(Cipher.ENCRYPT_MODE, clientPub);
   	   		  encryptedText2 = cipher3.doFinal(messaggioDaCifrare);
   	   	  } catch(NoSuchAlgorithmException e) {
           } catch(NoSuchPaddingException e) { 
           } catch(InvalidKeyException e) {
           } catch(IllegalBlockSizeException e) {
           } catch(BadPaddingException e) {
           }
           String AESParams = byteArrayToHexString(encryptedText2);
           
           AesUtil aesUtil2=new AesUtil(128, 1000);
           String encrypted_msg_client=aesUtil2.encrypt(salt, iv, passphrase, msgtoClient.toString());
           
           JSONObject final_msg=new JSONObject();
           final_msg.put("encrypted_msg_client", encrypted_msg_client);
           final_msg.put("AESParams", AESParams);      
           
           PrintWriter pw=null;
           
           try{
             pw = response.getWriter();    	 	
           	 pw.println(final_msg);
           	 //pw.println(msgtoClient);
           	
           }catch(Exception ex)
             {
             pw.println("{");
             pw.println("\"successful\": false,");
             pw.println("\"message\": \""+ex.getMessage()+"\",");
             pw.println("}");
            return;
            }               	
           
           
           
           
           
           
           
           
           
           
           
    }
    
    
    
    
    
    
private static byte[] blockCipher(byte[] bytes, int mode, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
		
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 256;

		// another buffer. this one will hold the bytes that have to be modified in this step
		byte[] buffer = new byte[length];

		for (int i=0; i< bytes.length; i++){

			// if we filled our buffer array we have our block ready for de- or encryption
			if ((i > 0) && (i % length == 0)){
				//execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn,scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
				if (i + length > bytes.length) {
					 newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i%length] = bytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn,scrambled);

		return toReturn;
	}
	
	private static byte[] append(byte[] prefix, byte[] suffix){
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i=0; i< prefix.length; i++){
			toReturn[i] = prefix[i];
		}
		for (int i=0; i< suffix.length; i++){
			toReturn[i+prefix.length] = suffix[i];
		}
		return toReturn;
	}
       
    
	public static String byteArrayToHexString(byte[] bytes)          
    {         
        StringBuffer buffer = new StringBuffer();
        for(int i=0; i<bytes.length; i++)
        {                        
            if(((int)bytes[i] & 0xff) < 0x10)       
                buffer.append("0");                               
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }                        
        return buffer.toString();
    }   
	
    
	public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return Hex.encodeHexString(salt);
    }
    
	
}