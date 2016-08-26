package it.uninsubria.rulemanagerservice.springmvc.service;

import java.math.BigInteger;
import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.User;


public interface UserService {
	
	
	
	User findByEmail(String email);
	
	void deleteByEmail(String email);
	
	List<User> findAllUsers();
		
	void saveUser(User user);
			
	boolean isUserSSOUnique(String email);

}