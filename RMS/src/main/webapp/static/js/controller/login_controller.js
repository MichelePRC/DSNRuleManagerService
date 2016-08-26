'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){
     $window.onload=function (){   	  
    	  var ok="ok";
    	  LoginService.createRMS("casaaaaaaaaaaaaaa@gmail.com")
    	   .then(
			   		  function(ok){
		   				console.log("ciaoooooo");
		   			  },
		              function(error){
			               console.error('Error while updating User.');

		              }	
     );	 
     }
     
       /*self.updateUser=function(user, id){
            LoginService.updateUser(user, id)
		              .then(
				              function(errResponse){
					               console.error('Error while updating User.');

				              }	
                );
        };
        
        
       self.deleteUser=function(id){
            LoginService.deleteUser(id)
		              .then(
				              function(errResponse){
					               console.error('Error while deleting User.');

				              }	
                );
        };
        
        self.login=function(user){
         	var key = CryptoJS.enc.Base64.parse("MTIzNDU2NzgxMjM0NTY3OA==");
            var iv  = CryptoJS.enc.Base64.parse("EBESExQVFhcYGRobHB0eHw==");
            var u={id:null,birth_day:'',city:'',email:'',firstname:'',lastname:'',photo:'',pw:''};
           u.email=self.user.email;
             u.pw=self.user.pw;
           
      	  LoginService.login(u)
      	  	.then(
      	  			function(d){
      	  		 
      	  		  self.user=d;
      	  		 var decrypted = CryptoJS.AES.decrypt(self.user.pw, key, {iv: iv});
      	  		
              if((decrypted.toString(CryptoJS.enc.Utf8))==u.pw){
            	  
            		$window.location.href=url+'/profile/'+self.user.id+'?'+self.user.lastName+self.user.firstName;

               }else {
            	   self.message='Error username or password, please repeat';
               }
      	  			},
      	  			function(errResponse){
                 	   self.message='Error username or password, please repeat';

      	  			});
        };

        
       self.getUser=function(email){
      	  LoginService.getUser(email)
      	  .then(
      			  function(d){
      				  self.user=d;
      			  },
      			  function(errResponse){
    	  				console.error('Error while getUser..');
    	  				$q.reject(errResponse);
    	  			});
      			  
      	  
        };
        
        self.edit=function(id){
            console.log('id to be edited', id);
            for(var i = 0; i < self.users.length; i++){
                if(self.users[i].id == id) {
                   self.user = angular.copy(self.users[i]);
                   break;
                }
            }
        };
      
        
       self.reset=function(){
      	  self.user.email='';
      	  self.user.pw='';
            $scope.myForm.$setPristine(); //reset Form
        };
        
        self.submit=function(){
        	self.login(self.user);
        	self.reset();
        };
	
	*/
	
        
}]);