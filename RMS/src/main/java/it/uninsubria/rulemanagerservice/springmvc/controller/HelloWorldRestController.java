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
  
	
	
	
    @RequestMapping(value = "/createSocialUser/", method = RequestMethod.POST)
    public void createUser(@RequestBody String email, HttpServletResponse response ) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    	User newUser=new User();
    	newUser.setEmail(email);
    	newUser.setExponent("esponente");
    	newUser.setModulus("modulo");
    	newUser.setSecret("segreto");
    	
    	userService.saveUser(newUser);
    	System.out.println(email);
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);    	
    	PublicKey pub = factory.generatePublic(spec);
    	
    	String strPublicKey=byteArrayToHexString(pub.getEncoded());
    	System.out.println("Public Key : "+ strPublicKey);

    	File dir = new File("tmp/test");
    	dir.mkdirs();
    	File tmp = new File(dir, "RMSPublicKey.txt");
    	tmp.createNewFile();
    	//File file = new File("/home/utente.tomcat/prova.txt");
    	
    	PrintWriter writer = new PrintWriter("tmp/test/RMSPublicKey.txt", "UTF-8");
    	//PrintWriter writer = new PrintWriter("/home/utente.tomcat/prova.txt", "UTF-8");
    	writer.println(strPublicKey);
    	writer.close();
    	
    	
    	
    	//FileWriter w=new FileWriter("C:/Users/Michele/git/DSNRuleManagerService/RMS/src/main/resources/RMSPublicKey.txt");
    	FileWriter w=new FileWriter("tmp/test/RMSPublicKey.txt");

    	BufferedWriter b=new BufferedWriter(w);
		b.write(strPublicKey);
		b.flush();
    	
    	ByteArrayOutputStream ba=loadTxt("tmp/test/RMSPublicKey.txt");
    	//ByteArrayOutputStream ba=loadTxt("/home/utente.tomcat/prova.txt");
    	   	
    	PrintWriter pw = null;
    	
    	try{
    	pw = response.getWriter();    	
   
    	//String txtBase64String = org.apache.commons.codec.binary.StringUtils.newStringUtf8(org.apache.commons.codec.binary.Base64.encodeBase64(ba.toByteArray()));
    	String txtBase64String=Base64.getEncoder().encodeToString(ba.toByteArray());
    	
    	//wrting json response to browser
    	System.out.println("codifica Base64: "+ txtBase64String);
    	pw.println("{");
    	pw.println("\"successful\": true,");
    	pw.println("\"txt\": \""+txtBase64String+"\"");
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
		 KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
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
	     }
	   	 
	   	 
	   	 String modulo=pub.getModulus().toString(16);
	   	 String esponentePubblico=pub.getPublicExponent().toString(16);
    	
	   	 JSONObject chiave = new JSONObject();
	   	 chiave.put("modulo", modulo);
	   	 chiave.put("esponente_pubblico", esponentePubblico);
	   	 System.out.println(chiave);
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
        
        System.out.println(sb.toString());
    	
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
    
    	
    	RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger("28635536705033851965763623976468658784768396084495616942930828137000741224233792755873148732667120420878566403708741853052969706379986497583240992872284572036486095447017003541902594327787306191267469476787690198822719335304482660609167607387922531723654492936023166184148984218920758296119081763087878578889367563504426181839382162424543295974186501012450396497408257754275466689308091378265909380366095664550760205039516267803738826629447870278776199795801725408894761458701893258825205307188260404385967469957029313478081957370431674473293581006031260199283259713094295739600236422843110090332957477541346010466693"), new BigInteger("3"));
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PublicKey pub = factory.generatePublic(spec);
    	
    	RSAPrivateKeySpec spec2 = new RSAPrivateKeySpec(new BigInteger("28635536705033851965763623976468658784768396084495616942930828137000741224233792755873148732667120420878566403708741853052969706379986497583240992872284572036486095447017003541902594327787306191267469476787690198822719335304482660609167607387922531723654492936023166184148984218920758296119081763087878578889367563504426181839382162424543295974186501012450396497408257754275466689308091378265909380366095664550760205039516267803738826629447870278776199795801725408894761458701893258825205307188260404385967469957029313478081957370431674473293581006031260199283259713094295739600236422843110090332957477541346010466693"), new BigInteger("19090357803355901310509082650979105856512264056330411295287218758000494149489195170582099155111413613919044269139161235368646470919990998388827328581523048024324063631344669027935062885191537460844979651191793465881812890202988440406111738258615021149102995290682110789432656145947172197412721175391919052592685967395417976001425427402814838018196585346014861647589906892428288736642951057006326712419935594589823379348476712811610584824427575954373140024497067140763080697590123218261605294727192273430204506741495765672866472346589603582129854958404546900712108479962110429754278479449059923854008260170802559104619"));
    	KeyFactory factory2 = KeyFactory.getInstance("RSA");
    	PrivateKey priv = factory.generatePrivate(spec2);
    	
    	
    	
    	String text="proviamo";    	
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
          System.out.println(messaggioCifrato);

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