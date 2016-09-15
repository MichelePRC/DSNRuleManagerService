package it.uninsubria.rulemanagerservice.springmvc.dao;

import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.User;


public interface UserDao {

	
	User findByIdu(Integer idu);
	
	void save(User user);
	
	void deleteByIdu(Integer idu);
	
	List<User> findAllUsers();	

}

