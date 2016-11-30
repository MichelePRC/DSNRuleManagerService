<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>  
    <title>Welcome to DSNProject</title>  
    <style>
      .username.ng-valid {
          background-color: lightgreen;
      }
      .username.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .username.ng-dirty.ng-invalid-minlength {
          background-color: yellow;
      }
      .email.ng-valid {
          background-color: lightgreen;
      }
      .email.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .email.ng-dirty.ng-invalid-email {
          background-color: yellow;
      }
    </style>
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
     <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
     

     
     
     
     
  </head>
  <body ng-app="myApp" class="ng-cloak">
 
      <div class="generic-container" ng-controller="LoginController as ctrl">
        <h1 style="text-align:center;">Welcome to DSNProject!</h1>
          <div class="panel panel-default">
              <div class="panel-heading"><span class="lead">Login </span></div>
              <div class="formcontainer">
                <div id='dataFormContainer'>
				</div> 
				
				<div id='dataPhoto'>
				</div>
              </div>
            
              
          </div>
        

   	  


   	  <script type="text/javascript" src="static/js/libraries/excluded/jquery-2.1.3.min.js"></script>
	  <script type="text/javascript" src="static/js/libraries/rollups/aes.js"></script>
      <script type="text/javascript" src="static/js/libraries/rollups/pbkdf2.js"></script>
      <script type="text/javascript" src="static/js/AesUtil.js"></script> 
      
     	 
   	  <script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
	  <script src="https://rawgit.com/andris9/jStorage/master/jstorage.js"></script>
	  
	  <script src="http://www-cs-students.stanford.edu/~tjw/jsbn/prng4.js"></script>
	  <script src="http://www-cs-students.stanford.edu/~tjw/jsbn/rng.js"></script>	  
	  <script src="http://www-cs-students.stanford.edu/~tjw/jsbn/jsbn.js"></script>	
	  <script src="http://www-cs-students.stanford.edu/~tjw/jsbn/jsbn2.js"></script>		  
	  <script src="http://www-cs-students.stanford.edu/~tjw/jsbn/rsa.js"></script>
	  <script src="http://www-cs-students.stanford.edu/~tjw/jsbn/rsa2.js"></script>	  
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular-route.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
      <script src="<c:url value='/static/js/service/login_service.js' />"></script>
      <script src="<c:url value='/static/js/controller/login_controller.js' />"></script>
  </body>
</html>