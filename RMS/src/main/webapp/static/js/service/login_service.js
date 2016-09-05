'use strict';

App.factory('LoginService',['$http','$q',function($http,$q){

	return{
		
		
		download:function(){
	    	return $http.post('http://localhost:8080/RMS/createSocialUser/', 'blaaaaaaaaaaaa@gmail.com').then(
	    			function(response){
	    				console.log(response.data);
	    				return response.data;
	    			},
	    			function(errResponse){
	    				console.error('Error while inviate email RMS');
	    				return $q.reject(errReponse);
	    			});
		},
		
		
		
		
		login:function(){
			    	return $http({
			            url : 'http://localhost:8080/RMS/loginRequest/',
			            method : "POST",
			            data : {
			                'user_email' : "m.procopio91@gmail.com",
			                'nonce': Math.floor((Math.random() * 100) + 1),
			                'message': 'Login Request'
			            }
			        }
			    			).then(
			    			function(response){
			    				console.log(response.data);
			    				return response.data;
			    			},
			    			function(errResponse){
			    				console.error('Error login');
			    				return $q.reject(errReponse);
			    			});
		},
		
		pubKeys:function(){
	    	return $http({
	            url : 'http://localhost:8080/RMS/getPublicKeys/',
	            method : "GET",	            
	        }
	    			).then(
	    			function(response){
	    				return response.data;
	    			},
	    			function(errResponse){
	    				console.error('Error pubKeys');
	    				return $q.reject(errReponse);
	    			});
		},
		
		decrypt:function(txtCrypted){
	    	return $http({
	    		url: 'http://localhost:8080/RMS/decryptRequest/',
	            method: "POST",
	            data: txtCrypted,	            
	        }
	    			).then(
	    			function(response){
	    				return response.data;
	    			},
	    			function(errResponse){
	    				console.error('Error decrypt');
	    				return $q.reject(errReponse);
	    			});
		},
		
		
		getMessageToDecrypt:function(){
	    	return $http({
	    		url: 'http://localhost:8080/RMS/encryptRequest/',
	            method: "GET"            
	        }
	    			).then(
	    			function(response){
	    				return response.data;
	    			},
	    			function(errResponse){
	    				console.error('Error encrypt');
	    				return $q.reject(errReponse);
	    			});
		},
	
		clientPubKeys:function(dataToSend){
	    	return $http({
	    		url: 'http://localhost:8080/RMS/clientPubKeys/',
	            method: "POST",
	            data: dataToSend
	        }
	    			).then(
	    			function(response){
	    				return response.data;
	    			},
	    			function(errResponse){
	    				console.error('Error encrypt');
	    				return $q.reject(errReponse);
	    			});
		}
		
		
		
	
	}}



]);