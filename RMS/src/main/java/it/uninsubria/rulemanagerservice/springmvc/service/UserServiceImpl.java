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
	

	public User findByIdu(Integer idu) {
		User user = dao.findByIdu(idu);
		return user;
	}

	public void saveUser(User user) {
		dao.save(user);
	}
	
	public void deleteByIdu(Integer idu) {
		dao.deleteByIdu(idu);
	}

	public List<User> findAllUsers() {
		return dao.findAllUsers();
	}

	public boolean isUserSSOUnique(Integer idu) {
		User user = findByIdu(idu);
		return ( user == null || ((idu != null) && (user.getIdu() == idu)));
	}
		
	
}
