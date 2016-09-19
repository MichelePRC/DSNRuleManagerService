package it.uninsubria.rulemanagerservice.springmvc.service;

import java.math.BigInteger;
import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.User;


public interface UserService {
	
	
	
	User findByIdu(Integer idu);
	
	void deleteByIdu(Integer idu);
	
	List<User> findAllUsers();
		
	void saveUser(User user);
	
	void updateUser(User user);
			
	boolean isUserSSOUnique(Integer idu);

}