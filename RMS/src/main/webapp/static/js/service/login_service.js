'use strict';

App.factory('LoginService',['$http','$q',function($http,$q){
	
	return{
		
		
		download:function(){
			    	return $http.post('http://localhost:8080/RMS/createSocialUser/', 'e@gmail.com').then(
			    			function(response){
			    				console.log(response.data);
			    				return response.data;
			    			},
			    			function(errResponse){
			    				console.error('Error while inviate email RMS');
			    				return $q.reject(errReponse);
			    			});
		}}}



]);

