import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.RandomStringUtils;

import it.uninsubria.rulemanagerservice.springmvc.controller.AesUtil;


public class Prova {
	
	
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

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// TODO Auto-generated method stub
		
		//genero la coppia di chiavi asimmetriche di RMS
		 KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		 kpg.initialize(2048);
		 KeyPair kp = kpg.genKeyPair();
	
	     //genero le specifiche della sua chiave pubblica per accedere a modulus e publicExponent   	
	     /*KeyFactory fact = KeyFactory.getInstance("RSA");
	   	 RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),RSAPublicKeySpec.class);*/  
	   	 
	   	 
		 PublicKey pubKey=kp.getPublic();		 
		 PrivateKey privKey=kp.getPrivate();
	   	 	     
	     KeyFactory fact;
	     
	     RSAPublicKeySpec pub = new RSAPublicKeySpec(BigInteger.ZERO, BigInteger.ZERO);
	     try {
	         fact = KeyFactory.getInstance("RSA");
	         pub = fact.getKeySpec(pubKey,    RSAPublicKeySpec.class);
	     } catch(NoSuchAlgorithmException e1) {
	     } catch(InvalidKeySpecException e) {
	     }
	   	 
	   	 
	     RSAPrivateKeySpec priv = new RSAPrivateKeySpec(BigInteger.ZERO, BigInteger.ZERO);
	     try {
	         fact = KeyFactory.getInstance("RSA");
	         priv = fact.getKeySpec(privKey,    RSAPrivateKeySpec.class);
	     } catch(NoSuchAlgorithmException e1) {
	     } catch(InvalidKeySpecException e) {
	     }
	     
	    long startTime=System.currentTimeMillis();
	    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; 
	    System.out.println(RandomStringUtils.random(16, characters));
	    System.out.println(System.currentTimeMillis()-startTime);
	    
	   	System.out.println(new AesUtil(128,1000).random(128/8).toString());
	   	System.out.println((new AesUtil(128,1000).random(128/8).toString())+(new AesUtil(128,1000).random(128/8).toString()));
	   	
	   	
	   	//HEX DI KMS
	   	/*System.out.println(new BigInteger("17737503101749240551514336479175933996284643920669788811312613471326716253010628375353723853986766905314693217598509518089927623057672662993697758913998354212580704554075747909087014639864811176837711211735839780607167060461547827999273857020081797574786231715194107237622619461176375228673135495556249692971274924023580556404585231794637608393141731253345224810692693563065544642738343947617734184996519459950765704772330742411359297713086295820292163619327648556542040781243944099727463385748877998477799878503638845600384955441485035714356463911939709035706287370046954645803349441627089333535720135818419498336743").toString(16));
	   	System.out.println(new BigInteger("65537").toString(16));
	   	System.out.println(new BigInteger("12066059833104729271520090161506042226869737633265186456290933881298925248206659356241813278887316205696624090620210741039186311414272283772297076276055886407758563413488639902389753676159314767013730278188003861925762880349981312355823841397520281665000225556964519109322850005617668804563921550618562396997627074269273576277658828705277569753807247173968105151569652637519041933739243056323640277544949801657374859164498222716713677770077855390959065922906308481036228006987998242643345401571480094493501740978535780847873720846400352416116881443330336813824343879052966843094300925809192217060212460594863648009921").toString(16));
	   	*/
	   	
	   	//HEX DI RMS
	   	System.out.println(new BigInteger("17714908574856389042047912980040795159637941050288872641206841191521734706784824832587434981882447608061483516636172075038361031641464335903337528243013601798968698688028612808889155394216948237201735891627985050112232818125882516408062750119579076532728058982496002319380681963642398518248582531197827290636948450219681285816430149218145081558444117686831521075752620790347565538493795037354011541355760251161663100066112133754391379261720042326840236102811977234477719273601679510118145984721644619247954955084233630790301208917025621493044134461979822724888691405830185658708289093804087754091527782031674415494421").toString(16));
	   	System.out.println(new BigInteger("65537").toString(16));
	   	
	   	
	   	
	   	System.out.println("da hex a biginteger: " + new BigInteger("a8d3c3e59aaa12b6d1a6e719f0c4855165be24485c1b141a0e7f3cad8d86a9d54d835ba40c2605f9d7e5347ba5c6e94218c0b97ba204c8e72673d4db86a5516ade3db22ba54dccd0838205a0fc31ae5fa49aae905496ed57546ad88052ba0e44ff9b5e8d85521a1c3c56c0eb21f39688e06fb8980b72d5bfdebfdc2d3578beea141828f0563ffe69790477398f24a181374e137839867819935ce60d02465c34b763e5f983af2f2b254a2390356a4a0bac87f0ec61b922991ef0569c7cfe4ed21ae6abf7a0b6dc419d9c183840d10d8e9f45c24309b7e9c88e0cb5a03055fff33ca510c3fa42ed1bfb3b741891706a3d3f1f1a01b5353b57cebb27dcccdcc973", 16));
	   	
	   	String modulo=pub.getModulus().toString(16);
	   	String esponentePubblico=pub.getPublicExponent().toString(16);
	   	String esponentePrivato=priv.getPrivateExponent().toString(16);

	   	System.out.println("modulo: "+ pub.getModulus());
		System.out.println("espPubblico: "+ pub.getPublicExponent());
		System.out.println("espPrivato: "+ priv.getPrivateExponent());
		
		System.out.println();
		
		System.out.println("modulo (esadecimale): "+ modulo);
		System.out.println("espPubblico: "+ esponentePubblico);
		System.out.println("espPrivato(esadecimale): "+ esponentePrivato);
		
		
		
		RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger("23076546670056382309675054116609040477208471004209380863855606650960962742115843277262108558426555916075399719555489895433024050073799569649162293863507972661326664599854138870797227933079861523403768730506679363954584828292063156824702424833432690179070099913575305738144033153438097384309193716715004310414080605968132393258029991591883815882977877481907553110366277146252057636898463148391247124533831919464487629013848318704585424925854086871697115584305352479303356052261633569037370265192429688221743912721192578332941912747857793485050298571142861008809163831625486021237824752427873536844232165227271010703893"), new BigInteger("65537"));
    	KeyFactory factory = KeyFactory.getInstance("RSA");
    	PublicKey pub2 = factory.generatePublic(spec);
    	
    	String text="proviamoaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";    	
        Cipher cipher;

        byte[] encryptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          byte[] messaggioDaCifrare =text.getBytes();
          cipher.init(Cipher.ENCRYPT_MODE, pubKey);
          encryptedText = cipher.doFinal(messaggioDaCifrare);
          } catch(NoSuchAlgorithmException e) {
          } catch(NoSuchPaddingException e) { 
          } catch(InvalidKeyException e) {
          } catch(IllegalBlockSizeException e) {
          } catch(BadPaddingException e) {
          }
        
        String chipertext = new String(Base64.getEncoder().encode(encryptedText));
        System.out.println("\nencrypted (chipertext) = " + chipertext);
        		
        byte[] ciphertextBytes = Base64.getDecoder().decode(chipertext.getBytes());
		byte[] decryptedText = new byte[1];
        try {
          cipher = javax.crypto.Cipher.getInstance("RSA");
          cipher.init(Cipher.DECRYPT_MODE, privKey);
          decryptedText = cipher.doFinal(ciphertextBytes);
          } catch(NoSuchAlgorithmException e) {
          } catch(NoSuchPaddingException e) { 
          } catch(InvalidKeyException e) {
          } catch(IllegalBlockSizeException e) {
          } catch(BadPaddingException e) {
          }
        String decryptedString = new String(decryptedText);
        System.out.println(new String(decryptedString));
	}

}
