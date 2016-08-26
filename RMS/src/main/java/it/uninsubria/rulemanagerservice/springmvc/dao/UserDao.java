package it.uninsubria.rulemanagerservice.springmvc.dao;

import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.User;


public interface UserDao {

	
	User findByEmail(String email);
	
	void save(User user);
	
	void deleteByEmail(String email);
	
	List<User> findAllUsers();	

}

