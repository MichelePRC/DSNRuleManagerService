'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){
	 var modulo="ad4071de41593092e891f6b4a09d68c50493bc3adcc3e177b27632fb4ae6ae04bc11decdc5b162a0550b8a4592285f11fdab5fe2d3ee1bc26a25f694844501dc0b652d235ab58584ef970f3b5edc4dffc466faf736d87ab0c06fe36020ad37e0eb6e99b7f63abfa0facae9ef5e6dba8a9d959e329762239d9770708cae92fb01f0fcc7d9f9aad3b8c57df9b76d6f6c91ab1b730fae7759899e14164e93e2489e667bb6bfc18cb1011260480a102bb57d562b8ac7654d7daabf52617622eb2d85d93eff47873a92fb7a495ea1a8f4e47616d06fed3529087bd307d5222bf1a2c21c6e7fd9a49e6b1f2bff27f14483fc0044464c36890a1d9c2ce349fcf6a86521";
	 var esponente_pubblico="10001"
	 var esponente_privato="5f7f1244cc087979c0b01428640fdfdc1935c59f3c68cc32ca2fd4d226eb18288bf1ddb9d6aa7c0e9c520c196502d3c47aeaa7a3c3e58f6f8e4af6abce6cab25bb323dbb2b18bb4c173450b50d08aa05bd20765d25e2155eaff3fc84d3fe26bad78ad3384f84fa73a5498bc0e16fe24edda5c6d964103849795c04edc69c45b654c1c7f4892c0fb43befe209fdbb6d1c7d4ee1a436c494e5be4cbd387994b82e1ece3bf69bbb0632f5cc5cbe947d03b1799e6aefe90362b6089847f1a2f488a9e80c76089ea173d3425c6e0825030f263eedcd2aa4ac450a996d7148d8aae4dde4710e9beba0e524568d0076a701101c02e5525bf169281df69013ed150cbc31";
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
    	 var iv = "F27D5C9927726BCEFE7510B1BDD3D137";
    	 var salt = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    	 var plainText ="funziona";
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
					console.log(messaggioCifrato);
    	     	           	 
    	 
					LoginService.clientPubKeys(messaggioCifrato)
						.then(
								function(data){
									console.log="ok";     			
								},
								function(errResponse){
									console.error('Error clientpubkeys');
								}
						);
    
				 },
				 function(errResponse){
					console.error('Error clientpubkeys');
				 }
			 );

	
	
        
}}]);