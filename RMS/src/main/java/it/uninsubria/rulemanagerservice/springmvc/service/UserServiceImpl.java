package it.uninsubria.rulemanagerservice.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.rulemanagerservice.springmvc.dao.UserDao;
import it.uninsubria.rulemanagerservice.springmvc.model.User;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao dao;
	

	public User findByEmail(String email) {
		User user = dao.findByEmail(email);
		return user;
	}

	public void saveUser(User user) {
		dao.save(user);
	}
	
	public void deleteByEmail(String email) {
		dao.deleteByEmail(email);
	}

	public List<User> findAllUsers() {
		return dao.findAllUsers();
	}

	public boolean isUserSSOUnique(String email) {
		User user = findByEmail(email);
		return ( user == null || ((email != null) && (user.getEmail() == email)));
	}
		
	
}
