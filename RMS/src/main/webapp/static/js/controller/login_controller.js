'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){
	 var modulo;
	 var esponente_pubblico;
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
        		 
		 LoginService.pubKeys()
		 .then(
				 function(data){					 
 					modulo=data.modulo;
    				esponente_pubblico=data.esponente_pubblico;
    				//console.log(modulo);
    				var prova={
			                'user_email' : "m.procopio91@gmail.com",
			                'nonce': Math.floor((Math.random() * 100) + 1),
			                'message': 'Login Request'
			            };
    				
    				var jsontoString=JSON.stringify(prova);   				
    				
    				var rsa = new RSAKey();
    				rsa.setPublic(modulo, esponente_pubblico);
    				var encry=rsa.encrypt(jsontoString);
					console.log(encry);
	        		
					LoginService.decrypt(encry)
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
		 
		 

		 
		 /*$http({
	            url : 'http://localhost:8080/RMS/decipherReq/',
	            method : "POST",	            
	        }
	    			).then(
	    			function(response){
	    				modulo=response.data.modulo;
	    				esponente_pubblico=response.data.esponente_pubblico
	    				return response.data;
	    			},
	    			function(errResponse){
	    				console.error('Error while inviate email RMS');
	    				return $q.reject(errReponse);
	    			})*/
         	
    	 
    	 
    	 
    
     }

	
	
        
}]);