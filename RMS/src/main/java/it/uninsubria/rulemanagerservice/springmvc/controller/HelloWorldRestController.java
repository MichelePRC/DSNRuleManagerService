package it.uninsubria.rulemanagerservice.springmvc.controller;

import java.util.Base64;
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
import it.uninsubria.rulemanagerservice.springmvc.model.User;
import it.uninsubria.rulemanagerservice.springmvc.service.UserService;
 
@Controller
@RestController
public class HelloWorldRestController  {
	
	private PrivateKey privKey;
	
    @Autowired
    UserService userService;  
	
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
        
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(new BigInteger("17714908574856389042047912980040795159637941050288872641206841191521734706784824832587434981882447608061483516636172075038361031641464335903337528243013601798968698688028612808889155394216948237201735891627985050112232818125882516408062750119579076532728058982496002319380681963642398518248582531197827290636948450219681285816430149218145081558444117686831521075752620790347565538493795037354011541355760251161663100066112133754391379261720042326840236102811977234477719273601679510118145984721644619247954955084233630790301208917025621493044134461979822724888691405830185658708289093804087754091527782031674415494421"), new BigInteger("3193100309669019389866975846212397778671635833606397188009466637097307659661704621013402649510617721196122873827350973075181330649566171781224746648987895052431713957027053467680967890991116613913729741791680842836501614058029054829004154403811398615760815428845160373511817691327153420145421753223979336623450286456680659702981887841904851516103670703517747044018559696301849847656525215149550874762061740130122693552122371039672803732736921211104351033717193752619213654445454041991222502562351748180793135077431242863828024145583498865011149171566439944824817170908360572903125256698750197062617755564419422244029"));
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
	   	jsonmsg2.put("funziona", "sì");

	   	String doppiacifratura=jsonmsg2.toString();    	
        

	   	String iv2=new AesUtil(128,1000).random(128/8).toString();
	   	String salt2=new AesUtil(128,1000).random(128/8).toString();
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
    
    
    @RequestMapping(value = "/uploadReq/", method = RequestMethod.POST)
    public void uploadReq (HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JSONException{
    	
    	
    	
    	StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject messaggioInChiaro = new JSONObject(sb.toString());
        
        String paramRMS=messaggioInChiaro.getString("paramRMS");			//ACCEDO ALLA KEYSIMM CIFRATA TRAMITE CHIAVE PUBBLICA DI RMS
        
        RSAPrivateKeySpec spec = new RSAPrivateKeySpec(new BigInteger("17714908574856389042047912980040795159637941050288872641206841191521734706784824832587434981882447608061483516636172075038361031641464335903337528243013601798968698688028612808889155394216948237201735891627985050112232818125882516408062750119579076532728058982496002319380681963642398518248582531197827290636948450219681285816430149218145081558444117686831521075752620790347565538493795037354011541355760251161663100066112133754391379261720042326840236102811977234477719273601679510118145984721644619247954955084233630790301208917025621493044134461979822724888691405830185658708289093804087754091527782031674415494421"), new BigInteger("3193100309669019389866975846212397778671635833606397188009466637097307659661704621013402649510617721196122873827350973075181330649566171781224746648987895052431713957027053467680967890991116613913729741791680842836501614058029054829004154403811398615760815428845160373511817691327153420145421753223979336623450286456680659702981887841904851516103670703517747044018559696301849847656525215149550874762061740130122693552122371039672803732736921211104351033717193752619213654445454041991222502562351748180793135077431242863828024145583498865011149171566439944824817170908360572903125256698750197062617755564419422244029"));
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
          
          int idu=jsonmsgRMS.getInt("idu");
          int idresource=jsonmsgRMS.getInt("idR");
          int n1=jsonmsgRMS.getInt("n1");
          String msgKMS=jsonmsgRMS.getString("msgKMS");								//ACCEDO ALLA STRINGA DEL MSG CIFRATO TRAMITE CHIAVE PUBBLICA DI KMS
          
          

          
          dectyptedText = new byte[1];
          try {
            cipher = javax.crypto.Cipher.getInstance("RSA");
            
            byte[] messaggioCifratoBytes = new byte[256];

            BigInteger messaggioCifrato = new BigInteger(msgKMS.toString(), 16);
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
            messaggioDecifrato = new String(dectyptedText);
            JSONObject jsonmsgKMS = new JSONObject(messaggioDecifrato);				//MSG IN CHIARO DESTINATO A KMS
   
            System.out.println(jsonmsgKMS);
          
          
    	
    	
    }
    
    
    private static byte[] blockCipher(byte[] bytes, int mode, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException{
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
		int length = (1024 / 8 ) - 11 ;

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
	
	
	private ByteArrayOutputStream loadTxt(String pathTxt) throws FileNotFoundException
	{
		File file = new File(pathTxt);
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); //no doubt here is 0
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
	return bos;
	}
    
    
    
	
}