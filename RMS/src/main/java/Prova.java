import java.io.UnsupportedEncodingException;
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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	
	public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return Hex.encodeHexString(salt);
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
	
	
	
	
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, JSONException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, DecoderException {
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
	    
	   	
	   	System.out.println("chiavi di KMS E RMS");
	   	//HEX DI KMS
	   	System.out.println("HEX DI KMS");
	   	System.out.println(new BigInteger("17737503101749240551514336479175933996284643920669788811312613471326716253010628375353723853986766905314693217598509518089927623057672662993697758913998354212580704554075747909087014639864811176837711211735839780607167060461547827999273857020081797574786231715194107237622619461176375228673135495556249692971274924023580556404585231794637608393141731253345224810692693563065544642738343947617734184996519459950765704772330742411359297713086295820292163619327648556542040781243944099727463385748877998477799878503638845600384955441485035714356463911939709035706287370046954645803349441627089333535720135818419498336743").toString(16));
	   	System.out.println(new BigInteger("65537").toString(16));
	   	System.out.println(new BigInteger("12066059833104729271520090161506042226869737633265186456290933881298925248206659356241813278887316205696624090620210741039186311414272283772297076276055886407758563413488639902389753676159314767013730278188003861925762880349981312355823841397520281665000225556964519109322850005617668804563921550618562396997627074269273576277658828705277569753807247173968105151569652637519041933739243056323640277544949801657374859164498222716713677770077855390959065922906308481036228006987998242643345401571480094493501740978535780847873720846400352416116881443330336813824343879052966843094300925809192217060212460594863648009921").toString(16));
	   	
	   	
	   	//HEX DI RMS
	   	System.out.println(new BigInteger("17714908574856389042047912980040795159637941050288872641206841191521734706784824832587434981882447608061483516636172075038361031641464335903337528243013601798968698688028612808889155394216948237201735891627985050112232818125882516408062750119579076532728058982496002319380681963642398518248582531197827290636948450219681285816430149218145081558444117686831521075752620790347565538493795037354011541355760251161663100066112133754391379261720042326840236102811977234477719273601679510118145984721644619247954955084233630790301208917025621493044134461979822724888691405830185658708289093804087754091527782031674415494421").toString(16));
	   	System.out.println(new BigInteger("65537").toString(16));
	   	
	   	System.out.println("RMS PRIV exp "+new BigInteger("3193100309669019389866975846212397778671635833606397188009466637097307659661704621013402649510617721196122873827350973075181330649566171781224746648987895052431713957027053467680967890991116613913729741791680842836501614058029054829004154403811398615760815428845160373511817691327153420145421753223979336623450286456680659702981887841904851516103670703517747044018559696301849847656525215149550874762061740130122693552122371039672803732736921211104351033717193752619213654445454041991222502562351748180793135077431242863828024145583498865011149171566439944824817170908360572903125256698750197062617755564419422244029").toString(16));
	   	
	   	System.out.println("da hex a biginteger: " + new BigInteger("a8d3c3e59aaa12b6d1a6e719f0c4855165be24485c1b141a0e7f3cad8d86a9d54d835ba40c2605f9d7e5347ba5c6e94218c0b97ba204c8e72673d4db86a5516ade3db22ba54dccd0838205a0fc31ae5fa49aae905496ed57546ad88052ba0e44ff9b5e8d85521a1c3c56c0eb21f39688e06fb8980b72d5bfdebfdc2d3578beea141828f0563ffe69790477398f24a181374e137839867819935ce60d02465c34b763e5f983af2f2b254a2390356a4a0bac87f0ec61b922991ef0569c7cfe4ed21ae6abf7a0b6dc419d9c183840d10d8e9f45c24309b7e9c88e0cb5a03055fff33ca510c3fa42ed1bfb3b741891706a3d3f1f1a01b5353b57cebb27dcccdcc973", 16));

	   	System.out.println();
	   	
	   	String modulo=pub.getModulus().toString(16);
	   	String esponentePubblico=pub.getPublicExponent().toString(16);
	   	String esponentePrivato=priv.getPrivateExponent().toString(16);

	   	System.out.println("modulo: "+ pub.getModulus());
		System.out.println("espPubblico: "+ pub.getPublicExponent());
		System.out.println("espPrivato: "+ priv.getPrivateExponent());
		System.out.println("espPrivato lunghezza: "+ priv.getPrivateExponent().toString().length());
		
		System.out.println();
		
		System.out.println("modulo (esadecimale): "+ modulo);
		System.out.println("espPubblico: "+ esponentePubblico);
		System.out.println("espPrivato(esadecimale): "+ esponentePrivato);
		
		
    	
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
        
        //-----------------
        System.out.println("////////////////////////////////////////////////////");
        
        JSONObject msg=new JSONObject();
        
        msg.put("elem1", random(16));
        msg.put("elem2", RandomStringUtils.random(16, characters));
        msg.put("elem3", esponentePrivato);
        
        System.out.println(msg.toString().getBytes().length);
        
        byte [] bytesmsg= msg.toString().getBytes("UTF-8");
        
        
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte [] encryptedmsg=blockCipher(bytesmsg,Cipher.ENCRYPT_MODE, cipher);
		
		char[] encryptedTranspherable = Hex.encodeHex(encryptedmsg);
		String strmsgencrypted=new String(encryptedTranspherable);
		
		
		byte[] bts = Hex.decodeHex(strmsgencrypted.toCharArray());
		
		Cipher cipher2 = Cipher.getInstance("RSA");
		
		cipher2.init(Cipher.DECRYPT_MODE, privKey);
		byte [] decryptedmsg=blockCipher(encryptedmsg,Cipher.DECRYPT_MODE, cipher2);	
		
		System.out.println(new JSONObject(new String(decryptedmsg,"UTF-8")).toString());
		
        
        


        
        
	}

}
