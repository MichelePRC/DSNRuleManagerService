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
					
				 });*/ 		 
		   	
    	 LoginService.getMessageToDecrypt()
		 .then(
				 function(data){
					 
					 var modulus="e2d64f24bbc841ad448d22bd0cb07bf5a36af19f8db7477326d60cd61b667e635fa489f66896cea1580aedcd80e17cfe318e024cc087a4e2db2349b0999c3761b0fa3134585b608e022ca847422687cb783c2b1e9bcb7b9d9e4a7584a3c1be03f7a0b4b4913859f0f492cf4bf624961f0a87eaed79d33ee13c1e96016c086a2129cea756693160e40f4059bae820947a5d0e1d961c69b0e6529696c1efbe1f564a6fbbdf1ad5d7b43b2b9e5bb8e9cad44f5193f81622ab1dcf9311e7d73e462ac6b2a478ca76dd63fdfe04840329219c99edfe29976dfb52ceb667c16066a3e49c0f6533d4043e6d8ef57200847d4a911761c61b0ae63a9c512e80db78d80985";
					 var expon="3"
					 var privat="97398a187d302bc8d85e1728b32052a3c2474bbfb3cf84f76f395de4124454423fc306a445b9df163ab1f3de55eba8a9765eac332b0518973cc23120666824ebcb517622e592405eac1dc584d6c45a87a57d721467dcfd13bedc4e586d2bd402a515cdcdb625914b4db734dd4ec30ebf5c5a9c9e51377f40d2bf0eab9d5af16a2fbcf49becbeb91b9c12a71f125edadacc1e2420a85200c6e6798050cc51f661bc58247910a2a40b3e5ec9ed613e906ff2e13d53f7508d34e7c6c90644df27693ee78fdebd3bbfced5f315eec45787bd137dad4514cecea1977c532d2dae19c9c169d9d2775a56e4106826f8b99b157eebb43b9c01119db7b9ea16668356b66b"
					 
					 
					 /*var passphrase="passphrase"
					 var bits=2048;
					 
					 var clientRSAKey=cryptico.generateRSAKey(passphrase,bits);*/

					 
					 /*console.log(clientRSAKey.n.toString(16));
					 console.log(clientRSAKey.e.toString(16));
					 console.log(clientRSAKey.d.toString(16));*/
					 
					 
					 var rsa = new RSAKey();
	    			 rsa.setPrivate(modulus, expon, privat);
	    			 var messaggioDecifrato=rsa.decrypt(data);
					 console.log(messaggioDecifrato);
        		        			
        		 },
        		 function(errResponse){
        			 console.error('Error while getUser...');
        });
    	 
    	 
    	 
    
     }

	
	
        
}]);