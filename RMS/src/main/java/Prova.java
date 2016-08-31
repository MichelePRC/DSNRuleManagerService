import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;


public class Prova {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// TODO Auto-generated method stub
		
		//genero la coppia di chiavi asimmetriche di RMS
		 KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		 kpg.initialize(2048);
		 KeyPair kp = kpg.genKeyPair();
	
	     //genero le specifiche della sua chiave pubblica per accedere a modulus e publicExponent   	
	     KeyFactory fact = KeyFactory.getInstance("RSA");
	   	 RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),RSAPublicKeySpec.class);  
	   	 String modulo=pub.getModulus().toString();
	   	 String esponentePubblico=pub.getPublicExponent().toString();
		
		System.out.println(modulo);
		System.out.println(esponentePubblico);
		
		
		
		

	}

}
