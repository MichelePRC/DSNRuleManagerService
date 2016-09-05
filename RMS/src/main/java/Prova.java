import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


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
