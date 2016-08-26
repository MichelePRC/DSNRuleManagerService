'use strict';

App.factory('LoginService',['$http','$q',function($http,$q){
	
	return{
		
		
		createRMS:function(email){
			    	//return $http.post('http://localhost:8080/RMS/keyBroadcast/').then(
					//return $http.post('http://193.206.170.143/RMS/createSocialUser/',email).then(
			    	return $http.post('http://localhost:8080/RMS/createSocialUser/',email)
			    	  .success(function() {
			      	    console.log("msg sent");
			      	  })
			      	  .error(function() {
			      	    console.log("msg failed");
			      	  });
		},
			
		    /*login:function(user){
				return $http.post(path+'/login/',user)
				
				.then(
						function(response){
							return response.data;
						},
						function(errResponse){
							console.error('Error while creating user');
							return $q.reject(errResponse);
						});
		    },
		    updateUser: function(user, id){
				return $http.put(path+'/user/'+id, user)
						.then(
								function(response){
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while updating user');
									return $q.reject(errResponse);
								}
						);
		},
	    
		deleteUser: function(id){
				return $http.del(path+'/user/'+id)
						.then(
								function(response){
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while deleting user');
									return $q.reject(errResponse);
								}
						);
		}*/
	
};
	}



]);


