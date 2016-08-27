'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){

     $window.onload=function (){
    	 
    	var ciao="ciao";
    	console.log(ciao);
    	
        LoginService.download()
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
        			
        		 });
         	
    	/*Lockr.set('username', 'Coyote');
    	console.log(Lockr.get('username'));
    	 */
     },
     function readTextFile(file)
 	{
 	    var rawFile = new XMLHttpRequest();
 	    rawFile.open("GET", file, false);
 	    rawFile.onreadystatechange = function ()
 	    {
 	        if(rawFile.readyState === 4)
 	        {
 	            if(rawFile.status === 200 || rawFile.status == 0)
 	            {
 	                var allText = rawFile.responseText;
 	                alert(allText);
 	            }
 	        }
 	    }
 	    rawFile.send(null);
 	}
	
	
        
}]);