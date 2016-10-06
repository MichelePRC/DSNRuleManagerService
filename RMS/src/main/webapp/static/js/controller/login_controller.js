'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){
	 var KMSmodulo="8c82122dd58edd13c5009a635148c2f865384ba7742c5f7f6791c5e2076ab92a5d3f5c17c5f78fbe71aafcde906fbb29b790d3f69d8982c11cdffb61de7e87afb6e603b7c1d2c0ec26b0d6964c7f89a05688d3d9704f330c0354837d00280137460f73dc701d8758ab61be034c6a61deefc8f74afd77563c9ef528e1f9fdfef9174e3da64b131b1341ca17ce4ba483d76442e1c214ddc30a0c21ac8c777516feb7c5fc90016b99fa455c16b7ed4c1d09a3d0cbcce4ecfc09f3476ceae135e90fa86d669c28bc6e8f878ad67e8e01327fe12ea183e109735fe444fc9c05cdeac59ade3c3fcc20b6597ad8fdbf90de3d603f9493ae82089e78c00c1778e6c741e7";
	 var KMSesponente_pubblico="10001";
	 var KMSesponente_privato="5f94e414f5dfd798693d3dc92e34571d6e8ddb39309836e68e67e5bb1adc88d5bed5292aaaeafb87cbff4b4183f3ddd8451d6a73ab3b2e0d7c3d09decbe23b1f574625654d9eb6ca6e8f818a80c389b5dee7f96543373d3c80cf6f1b0b8e2b35949d526db47f01e159c485e3ec89b9c6544bdb9dd1caea89168123ac85c93936eb2c2cd783408f39fab563586fa5a974b4f45a8b4dd6ebeb2c873eff4ca64c80ab61cab54237ba3867fa44c79214c9f1d5e6907fc705a8b50435cee81123ee0589353f37336c753c10fba26bf7e460e461f8c5710a1f493adbcb71643226f25623872ee14149fc58ce3ee02c0c286b1790c8bfcc3d058cf4041fc8ff166beec1";

	 
	 /*self.getRMS=function(){
	 LoginService.pubKeys() 
  	 .then(
  			 function(data){		 
  				 //$.jStorage.set("RMSmodulo",data.modulo);
					
	    	 },function(errResponse){
			    		 
	    		 console.error('Error while receinving RMSPublic Keys');
					 	    				
	    	 }
	  );
	 }*/
	 

	 
	 
	 
	 
	 
	 $window.onload=function (){
   	 

	      	 
	     
		 
		 
        /*LoginService.download()
         .then(
        		 function(data){
        			 var url="data:application/txt;base64,"+data.txt;
						var a=document.createElement('a');
						var linkText = document.createTextNode("Scarica la Public Key");
						a.appendChild(linkText);
						a.setAttribute('href',url);
						a.title="Public Key";
						dataFormContainer.appendChild(a);
        			 
        			
        		 },
        		 function(errResponse){
        			 console.error('Error while getUser...');
        			
        		 });*/
    	 
    	 
         /*LoginService.login()
         .then(
        		 function(data){
        			 console.log(data);
        		        			
        		 },
        		 function(errResponse){
        			 console.error('Error while getUser...');
        			
        		 }),*/
        		 
		 /*LoginService.pubKeys()
		 .then(
				 function(data){					 
 					modulo=data.modulo;
    				esponente_pubblico=data.esponente_pubblico;
    				//console.log(modulo);
    				var messaggio={
			                'user_email' : "m.procopio91@gmail.com",
			                'nonce': Math.floor((Math.random() * 100) + 1),
			                'message': 'Login Request'
			            };
    				
    				var jsontoStringMessaggio=JSON.stringify(messaggio);   				
    				
    				var rsa = new RSAKey();
    				rsa.setPublic(modulo, esponente_pubblico);
    				var messaggioCifrato=rsa.encrypt(jsontoStringMessaggio);
					console.log(messaggioCifrato);
	        		
					LoginService.decrypt(messaggioCifrato)
					 .then(
							 function(data){
			        			 console.log(data);
			        		        			
			        		 },
			        		 function(errResponse){
			        			 console.error('Error while getUser...');
			        })
    				
				 },
				 function(errResponse){
					 console.error('Error while getUser...');
					
				 }); 		 
		   	
    	 /*LoginService.getMessageToDecrypt()
		 .then(
				 function(data){
					 console.log(data);
					 var rsa = new RSAKey();
	    			 rsa.setPrivate(modulo, esponente_pubblico, esponente_privato);
	    			 var messaggioDecifrato=rsa.decrypt(data);
					 console.log(messaggioDecifrato);
        		        			
        		 },
        		 function(errResponse){
        			 console.error('Error while getUser...');
        })*/

    	 /*var iv=CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
    	 var salt= CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
    	 
    	 var plainText ="funzionaaaaaaa";
    	 var keySize = 128;
    	 var iterationCount = 1000;
    	 var passPhrase = "passphrase";
    	 
    	 var aesUtil = new AesUtil(keySize, iterationCount);
    	 var encrypt = aesUtil.encrypt(salt, iv, passPhrase, plainText);
    	 
    	 LoginService.pubKeys()
		 .then(
				 function(data){					 
 					modulo=data.modulo;
    				esponente_pubblico=data.esponente_pubblico;
    				var dataToSend={
    				    	 "iv": iv,
    				    	 "salt": salt,
    				    	 "keySize": keySize,
    				    	 "iterationCount": iterationCount,
    				    	 "passPhrase": passPhrase,
    				    	 "cipherText": encrypt
    				    	 };
    				
    				var jsontoStringMessaggio=JSON.stringify(dataToSend);   				
    				
    				var rsa = new RSAKey();
    				rsa.setPublic(modulo, esponente_pubblico);
    				var messaggioCifrato=rsa.encrypt(jsontoStringMessaggio);
    	     	             	 
					LoginService.clientKeys(messaggioCifrato)
						.then(
								function(data){
									var rsa = new RSAKey();
				    				rsa.setPrivate("ad4071de41593092e891f6b4a09d68c50493bc3adcc3e177b27632fb4ae6ae04bc11decdc5b162a0550b8a4592285f11fdab5fe2d3ee1bc26a25f694844501dc0b652d235ab58584ef970f3b5edc4dffc466faf736d87ab0c06fe36020ad37e0eb6e99b7f63abfa0facae9ef5e6dba8a9d959e329762239d9770708cae92fb01f0fcc7d9f9aad3b8c57df9b76d6f6c91ab1b730fae7759899e14164e93e2489e667bb6bfc18cb1011260480a102bb57d562b8ac7654d7daabf52617622eb2d85d93eff47873a92fb7a495ea1a8f4e47616d06fed3529087bd307d5222bf1a2c21c6e7fd9a49e6b1f2bff27f14483fc0044464c36890a1d9c2ce349fcf6a86521",
				    						"10001", "5f7f1244cc087979c0b01428640fdfdc1935c59f3c68cc32ca2fd4d226eb18288bf1ddb9d6aa7c0e9c520c196502d3c47aeaa7a3c3e58f6f8e4af6abce6cab25bb323dbb2b18bb4c173450b50d08aa05bd20765d25e2155eaff3fc84d3fe26bad78ad3384f84fa73a5498bc0e16fe24edda5c6d964103849795c04edc69c45b654c1c7f4892c0fb43befe209fdbb6d1c7d4ee1a436c494e5be4cbd387994b82e1ece3bf69bbb0632f5cc5cbe947d03b1799e6aefe90362b6089847f1a2f488a9e80c76089ea173d3425c6e0825030f263eedcd2aa4ac450a996d7148d8aae4dde4710e9beba0e524568d0076a701101c02e5525bf169281df69013ed150cbc31");
				    				console.log(data);
				    				var strmsgdecifrato=rsa.decrypt(data.RSAEncryptedSimmKey);
				    				var msgdecifrato=JSON.parse(strmsgdecifrato.toString());
				    				console.log(msgdecifrato);

									
									
									
									
									
									
								},
								function(errResponse){
									console.error('Error clientpubkeys');
								}
						);
    
				 },
				 function(errResponse){
					console.error('Error clientpubkeys');
				 }
			 );*/
    	 	
    	 	/*//GENERAZIONE DEL MESSAGGIO DIRETTO A KMS DELLA PRIMA RICHIESTA DI UPLOAD
    	 	var idu=34;
    	 	var fileName="beautiful-photo3"
    	 	var n2=Math.floor(Math.random()*100+1);
    	 	console.log(n2);
    	 	var msgKms={"idu": idu  ,"idR":fileName,"n2":n2};
    	 	msgKms=JSON.stringify(msgKms);
    	 	
    	 	var hexMod="8c82122dd58edd13c5009a635148c2f865384ba7742c5f7f6791c5e2076ab92a5d3f5c17c5f78fbe71aafcde906fbb29b790d3f69d8982c11cdffb61de7e87afb6e603b7c1d2c0ec26b0d6964c7f89a05688d3d9704f330c0354837d00280137460f73dc701d8758ab61be034c6a61deefc8f74afd77563c9ef528e1f9fdfef9174e3da64b131b1341ca17ce4ba483d76442e1c214ddc30a0c21ac8c777516feb7c5fc90016b99fa455c16b7ed4c1d09a3d0cbcce4ecfc09f3476ceae135e90fa86d669c28bc6e8f878ad67e8e01327fe12ea183e109735fe444fc9c05cdeac59ade3c3fcc20b6597ad8fdbf90de3d603f9493ae82089e78c00c1778e6c741e7";
    	 	var hexExp="10001";
    	 	var rsa=new RSAKey();
    	 	rsa.setPublic(hexMod,hexExp);    	 	
			console.log(rsa.encrypt(msgKms));
			
			rsa.setPrivate("db7006be332a0df64a30fc70ec6c9559b52e386b5313df5aed84fd080deefd455a960233489331e164ac26d5c688707260b7c4e466fab4925375f9219b6042e34910a6549300f3369eab9c88ddc63fca7495c13b261ee171f13eb845e3ead7d161a47f78d8f0c07da0f4adeb30fd72d7585545ec5ae6a37d2b70233c08b6a592edd2c37801cc50cf8446bd9eb3170b40c88605cd556af05c36057a90cf106a5ac840d29bdc4a068b5957dd13a44315eb7e45ca02234b6fbd07e31a24ce74051051480209bcad25bd7fcc1882c64f73319ebff6ac0b6c58c1cf19d14f55fa0a76fd210704f2bd191fe81f7ac5a4c43b8340e4306dd665345b27691fba692005ad", "10001", "386b3b3097d30e37eea75da063091ff52aa2319b5686db7c736927ed7437a822b6c1aedb24a3c3c561aee965396047190148fe74830d9e73a8c5c24223c2c22fd1cf5814929f41e91f2e9c568c3eb6940519a3cee364392ea48a2db2ba80dcf0569c86b1b9855cd87013b8cba992e07e01961b1e43d8dcdc07d91316125f737e3384603ccbfc67d014128ec871e071524e1997247b4517e22d2d10370280fb122ff59d394924855da2a3562d5da4f129c15529607fa075506e1df2b54a767291a35040133e1ebce10b490800874c004213bf1f6e6291ae42c28b28d0f155e00eac7cb32ca0f94abbacec8ae42e2ca191a11b633ce7d919eb653f9db5c46c0801");
			console.log(JSON.parse(rsa.decrypt("1c8db3a2baf2d191abecc06d2289248ae8d83f5939dc3c37fba994a726e20ba100959da4aac91840fedc559579c93d0586e6a29b638bc3d3bd3cbad3088af62bca8a5e2b1bfe48f6493fe948cfad24fe92ded24c7745896a91d81824d5d6d6a34b5faf5a02f9d46a7413afd66feef2b18fc47bdb42017d23e8b99f4fd17723bd629f437cd1b98002e809c9b8234d227b2cee830de0ff0e5ad47e1d3b3806e25febbd1fbfd18d7bb3c09e918e50dd45c536970ce7095f92f3dda7abcfdf27da3cb341eecef82831d381758534927fdc4dae1b708432937abb2b96985d8d317b8ea2098dd5bfe73afb3c751844b153482933bca243a55090ca98eb262ef0bf745d")));
			*/
			
			/*//GENERAZIONE DEL MESSAGGIO DIRETTO A KMS DELLA SECONDA RICHIESTA DI UPLOAD CON RSC CIFRATA
			var strcryptedrsc="ffuhrwqanfvujytrhanqbuiohu454j523nbkj6kjkbnjk";
			var msgKMS2={"idu": idu, "idR": fileName, "n2+2": n2+2, "cryptedRsc": strcryptedrsc};
			var msgKMS2=JSON.stringify(msgKMS2);
			
			var iv=CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
	    	var salt= CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
	    	var passphrase="ciaobelloooooo";
			
			var aesUtil = new AesUtil(128, 1000);			
	    	var encryptedmsgKMS = aesUtil.encrypt(salt, iv, passphrase, msgKMS2);
	    	
	    	var asymmkey={"iv": iv, "salt": salt, "passphrase": passphrase};
	    	asymmkey=JSON.stringify(asymmkey);
	    	rsa.setPublic(hexMod,hexExp); 
	    	
	    	asymmkey=rsa.encrypt(asymmkey);
	    	
	    	var msgtoKMS={"encryptedmsgKMS": encryptedmsgKMS, "asymmkey": asymmkey};
	    	msgtoKMS=JSON.stringify(msgtoKMS);
	    	
			console.log(msgtoKMS);*/
			
			
	
			
			
			//GENERAZIONE DEL MESSAGGIO DIRETTO A KMS DI RICHIESTA GENERAZIONE DELLE CHIAVI ASIMM PER CLIENT
    	 	/*var hexMod="8c82122dd58edd13c5009a635148c2f865384ba7742c5f7f6791c5e2076ab92a5d3f5c17c5f78fbe71aafcde906fbb29b790d3f69d8982c11cdffb61de7e87afb6e603b7c1d2c0ec26b0d6964c7f89a05688d3d9704f330c0354837d00280137460f73dc701d8758ab61be034c6a61deefc8f74afd77563c9ef528e1f9fdfef9174e3da64b131b1341ca17ce4ba483d76442e1c214ddc30a0c21ac8c777516feb7c5fc90016b99fa455c16b7ed4c1d09a3d0cbcce4ecfc09f3476ceae135e90fa86d669c28bc6e8f878ad67e8e01327fe12ea183e109735fe444fc9c05cdeac59ade3c3fcc20b6597ad8fdbf90de3d603f9493ae82089e78c00c1778e6c741e7";
    	 	var hexExp="10001";
    	 	var rsa=new RSAKey();
    	 	rsa.setPublic(hexMod,hexExp);
    	 
    	 
    	 	var iv=CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
	    	var salt= CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
	    	var keySize = 128;
	    	var iterationCount = 1000;
	    	var passPhrase = "passphrase-client";
	    	var idu=50508;
	    	
	    	var msgtoKMS={
			    	 "iv": iv,
			    	 "salt": salt,
			    	 "keySize": keySize,
			    	 "iterationCount": iterationCount,
			    	 "passPhrase": passPhrase,
			    	 "iduser": idu
			    	 };
	    	
			
			var msgtoKMS=JSON.stringify(msgtoKMS);   				
			
			var encryptedmsgtoKMS=(rsa.encrypt(msgtoKMS));
			
			var msgtoRMS={"idu": idu, "encryptedmsgtoKMS": encryptedmsgtoKMS};			
			var msgtoRMS=JSON.stringify(msgtoRMS); 
			
			LoginService.createSocialUser1(msgtoRMS)
			.then(
					function(data){
						var aesUtil = new AesUtil(keySize, iterationCount);
				    	var decrypted = aesUtil.decrypt(salt, iv, passPhrase, data );
				    	
						var jsonmsg=JSON.parse(decrypted);
						console.log(jsonmsg);
						var client_modulus=jsonmsg.client_modulus;
						var client_pub_exponent=jsonmsg.client_public_exponent;
						
						var clientPubKeyToRMS={"client_pub_exp": client_pub_exponent, "client_mod": client_modulus, "idu": idu};
						clientPubKeyToRMS=JSON.stringify(clientPubKeyToRMS);
						
						var iv2=CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
						var salt2=CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
						
						clientPubKeyToRMS=aesUtil.encrypt(salt2, iv2, passPhrase, clientPubKeyToRMS);
						
						//CIFRO IL MSG TRAMITE CHIAVE PUBBLICA DI RMS, IL MESSAGGIO CONTIENE LA CHIAVE PUBBLICA DEL CLIENT
						rsa.setPublic("8c5440573592b8bbecd6ee1807b2524747a41081e2730149cf0597921996ce64ef1836a8fdf0e6530d04676dab2546a919e8e4b4e03731bb1aa3615e79f5d23f2906459af8a6e2f431ac39f49897b60adc7bef81bd01287e99857f325540c9527fcb33995e08e384143c4b915104cd9cc192d57d52cbee33ee4283e113ea781e25bdd15648f35392df3387a25d83a6b189f4e74570a7ab6555a99bb56cf0e7ca24c9bcc2d35b5b80c0dee492f00191596304f5b12893f2bafbacd5c3d74ef12c1ee92dd69e63f5a3cdbd773027de77af15407699492df933ff43b051a987da4c1876776257b483bf93132cd2f82484a60fbc0584b8f63d4435c918cfe05fb515", "10001")
						
						var symmKey={
							"iv": iv2,
					    	 "salt": salt2,
					    	 "keySize": keySize,
					    	 "iterationCount": iterationCount,
					    	 "passPhrase": passPhrase,							
						}
						
						symmKey=JSON.stringify(symmKey);
						var encrypted_symmKey=rsa.encrypt(symmKey);
						
						var encrypted_clientPubKeyToRMS={"clientPubKeyToRMS": clientPubKeyToRMS, "encrypted_symmKey": encrypted_symmKey };
						
						LoginService.createSocialUser2(encrypted_clientPubKeyToRMS)
						.then(
								function(data){									
								},
								function(errResponse){
									console.error('Error clientpubkeys');
								}
						);
						
					},
					function(errResponse){
						console.error('Error clientpubkeys');
					}
			);*/

		 	/*var iv=CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
	   	 	var salt= CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
	   	 	var passphrase="passphrase";
		 	var aesUtil = new AesUtil(128, 1000);
		 	var msgRMS={prova:"ok", "numero":31};

		 	
		 	msgRMS=aesUtil.encrypt(salt,iv,passphrase,JSON.stringify(msgRMS));
		 		
		 	var keyRMS={"iv":iv,"salt":salt,"passPhrase":passphrase};
		 	var rsa=new RSAKey();
			rsa.setPublic("8c5440573592b8bbecd6ee1807b2524747a41081e2730149cf0597921996ce64ef1836a8fdf0e6530d04676dab2546a919e8e4b4e03731bb1aa3615e79f5d23f2906459af8a6e2f431ac39f49897b60adc7bef81bd01287e99857f325540c9527fcb33995e08e384143c4b915104cd9cc192d57d52cbee33ee4283e113ea781e25bdd15648f35392df3387a25d83a6b189f4e74570a7ab6555a99bb56cf0e7ca24c9bcc2d35b5b80c0dee492f00191596304f5b12893f2bafbacd5c3d74ef12c1ee92dd69e63f5a3cdbd773027de77af15407699492df933ff43b051a987da4c1876776257b483bf93132cd2f82484a60fbc0584b8f63d4435c918cfe05fb515","10001");
			var keyRMSencrypt=rsa.encrypt(JSON.stringify(keyRMS));
			
			var messageToRMS={"keyRMSencrypt":keyRMSencrypt,"msgRMS":msgRMS};
			console.log(salt);
			console.log(iv)
			console.log(passphrase);
			console.log(msgRMS);
			console.log(JSON.parse(aesUtil.decrypt(salt,iv,passphrase,msgRMS)));
			messageToRMS=JSON.stringify(messageToRMS);
			LoginService.uploadReq2(messageToRMS)
				.then(
						function(data){
							
						},function(errResponse){
							console.error('Error while upload request two.');
						});*/
		 	
		 	/*self.getRMS();
		 	//$.jStorage.set("ciao", "ciaoooo");
		 	//$.jStorage.setTTL("ciao", 60000);
		 	console.log($.jStorage.get("ciao"));

		 	console.log($.jStorage.index());*/
	
	
        
}}]);