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
	   	
	   	
	   	
	   	System.out.println(new BigInteger("27701431034589290942710237821670823600667305194681692455378258194254477239215276883490405513613304705976726208518191834964150696991250015111363135534235399795666637994903092631992960105961294842548776803871491504398600681567398122466835316987911120821507455503398822754022577023215034756040935277968972497272013824819087474996092620532981730441727620753412199075849054973447369818359865205794899802226650559089074204175190782533879120687141844383059328984404033657568663603111506732376285259162459296905282739562506379282105946514347904080775963983303774699981164841342205673439426828252081178014347820701686826010029").toString(16));
	   	System.out.println(new BigInteger("65537").toString(16));
	   	System.out.println(new BigInteger("7122222758637556683776607218749002512645438340637907103973688917301492919736597883437040647334851828672472597365328477335641534466065928477447378332115697797533955631385585407465727417877654120526525308531587223234454147800641749905643759879858742173770551371473673854544462255537686125994320146387188711400091900491064759745180155819133930741602262011904953481276831593344645370165930786731455153586333817783846259040006853558693579397180528811817699355153030892171769114549395182939513039092518124569342010681144274482765556089404779985654501265853302724601133797968123144907819856877350532552434289374722672822273").toString(16));
	   	
	   	
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
