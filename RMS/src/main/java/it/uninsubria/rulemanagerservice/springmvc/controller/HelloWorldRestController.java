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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.json.JSONException;
import org.json.JSONObject;


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
 
    @Autowired
    UserService userService;  //Service which will do all data retrieval/manipulation work
	
	@Autowired
	MessageSource messageSource;
	
	
	@Value("${crypto.modulus}")
	private BigInteger modulus;
	
	@Value("${crypto.exponent.public}")
	private BigInteger publicExponent;
  
	
	
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
	
	
	
	//@ResponseStatus(value = HttpStatus.OK)
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
    
    
    @RequestMapping(value = "/loginRequest/", method = RequestMethod.POST)
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
    	
    	
    	
    	
    	
    }
	
	
}