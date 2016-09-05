package it.uninsubria.rulemanagerservice.springmvc.controller;

import java.util.Base64;




import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
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
	
	
	@Value("${crypto.modulus}")
	private BigInteger modulus;
	
	@Value("${crypto.exponent.public}")
	private BigInteger publicExponent;
	
	@Value("${crypto.exponent.private}")
	private BigInteger privateExponent;
  
	
	
	
    @RequestMapping(value = "/createSocialUser/", method = RequestMethod.POST)
    public void createUser(@RequestBody Integer idu, HttpServletResponse response ) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JSONException {
    	User newUser=new User();
    	newUser.setEmail(idu.toString());							//modificare idu nella tabella user
    	newUser.setExponent("esponente");
    	newUser.setModulus("modulo");
    	newUser.setSecret("segreto");
    	
    	userService.saveUser(newUser);
    	
    	/*String modulo=modulus.toString(16);
	   	String esponentePubblico=publicExponent.toString(16);
	    	   	
    	
	   	JSONObject keys = new JSONObject();
	   	keys.put("modulus_RMS", modulo);
	   	keys.put("exponent_RMS", esponentePubblico);
	   	keys.put("modulus_KMS", modulo);					   	//KMS modulus e public exponent
	   	keys.put("exponent_KMS", esponentePubblico);			//KMS modulus e public exponent
    	*/
    	
    	   	
    	PrintWriter pw = null;
    	
    	try{
    		pw.println("{");
        	pw.println("\"successful\": true,");
        	pw.println("}");
        	return;
        	}catch(Exception ex)
        	{
        	pw.println("{");
        	pw.println("\"successful\": false,");
        	pw.println("\"message\": \""+ex.getMessage()+"\",");
        	pw.println("}");
        	return;
        	}
    	
    	
   
	}
    
    
    /*@RequestMapping(value = "/loginRequest/", method = RequestMethod.POST)
    public void loginRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException{
    	
    	StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject messaggio = new JSONObject(sb.toString());
    	System.out.println(messaggio.getString("user_email"));
    	System.out.println(messaggio.getDouble("nonce"));
    	System.out.println(messaggio.getString("message"));
    	
    	PrintWriter pw = null;
    	
    	try{
        	pw = response.getWriter();    	

        	pw.println("{");
        	pw.println("\"successful\": true,");
        	pw.println("}");
        	return;
        	}catch(Exception ex)
        	{
        	pw.println("{");
        	pw.println("\"successful\": false,");
        	pw.println("\"message\": \""+ex.getMessage()+"\",");
        	pw.println("}");
        	return;
        	}
    	
    	
    	
    	
    	
    }*/
    
    
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
    
    	
    	RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger("21871005673239196546630580968462831307780469444887567939897040269323277972115493387540984165984578291334061693954186883497564820760294114586601170284492964146139601341591907492065262191508078944716985902273080075977250863395468651386500153820020784187286356376422405683468337530184935533499369993400065440663615072138870687957222077094903187094492955893981881798107102483194997547801580022125313577909988540108152634392752342472418061358322613613405745500147071774348964100102811374966359763933478950255285021671818822221975496075767203934308154253723625295088346417846682528316030599387281961857135797862333405488417"), new BigInteger("65537"));
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PublicKey pub = factory.generatePublic(spec);
    	
    	RSAPrivateKeySpec spec2 = new RSAPrivateKeySpec(new BigInteger("21871005673239196546630580968462831307780469444887567939897040269323277972115493387540984165984578291334061693954186883497564820760294114586601170284492964146139601341591907492065262191508078944716985902273080075977250863395468651386500153820020784187286356376422405683468337530184935533499369993400065440663615072138870687957222077094903187094492955893981881798107102483194997547801580022125313577909988540108152634392752342472418061358322613613405745500147071774348964100102811374966359763933478950255285021671818822221975496075767203934308154253723625295088346417846682528316030599387281961857135797862333405488417"), new BigInteger("12055300195921277080892978117777001055316259185301715126735137139158553083978517221287677373270471736517564805108580603009994836277902018635677261323481755906055311638672292998510238970444754013747293875729934917139969943532629042566579665785654375512787163552525794328541285456160651406261062325733310404481767461235364506711100606082332405978183913371074562925601118083106626924146272455899400281706938176681595505490176073476471203085419686996863290921094783918658451732117578867201727953770866436242646497771895231699344955343433269397332526102377825741135550383455556271315284756734869344298644794253301728984113"));
    	KeyFactory factory2 = KeyFactory.getInstance("RSA");
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
          } catch(NoSuchPaddingException e) { 
          } catch(InvalidKeyException e) {
          } catch(IllegalBlockSizeException e) {
          } catch(BadPaddingException e) {
          }
          String messaggioDecifrato = new String(dectyptedText);
          JSONObject messaggio = new JSONObject(messaggioDecifrato);
          System.out.println(messaggio);
    	
        
        String iv =messaggio.getString("iv");
        String salt = messaggio.getString("salt");
        String passphrase = messaggio.getString("passPhrase");
        int iterationCount = messaggio.getInt("iterationCount");
        int keySize = messaggio.getInt("keySize");
        
        /*String ciphertext = messaggio.getString("cipherText");
        
        AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String plaintext = aesUtil.decrypt(salt, iv, passphrase, ciphertext);
    	System.out.println(plaintext);*/
        
        
        
    	
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
	   	 
	   	JSONObject jsonmsg = new JSONObject();
	   	jsonmsg.put("client_modulus", clientModulus);
	   	jsonmsg.put("client_public_exponent", clientPublicExponent);
	   	jsonmsg.put("client_private_exponent", clientPrivateExponent);
	   	
	   	AesUtil aesUtil = new AesUtil(keySize, iterationCount);
        String encrypted_client_keys = aesUtil.encrypt(salt, iv, passphrase, jsonmsg.toString());
	    
        PrintWriter pw = null;
        try{
          	pw = response.getWriter();    	 	
        	pw.println(encrypted_client_keys);
        	}catch(Exception ex)
          	{
          	pw.println("{");
          	pw.println("\"successful\": false,");
          	pw.println("\"message\": \""+ex.getMessage()+"\",");
          	pw.println("}");
          	return;
          	} 
    	
    	
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