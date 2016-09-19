package it.uninsubria.rulemanagerservice.springmvc.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.uninsubria.rulemanagerservice.springmvc.model.User;



@Repository("userDao")
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDao {


	public User findByIdu(Integer idu) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("idu", idu));
		User user = (User)crit.uniqueResult();
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstname"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<User> users = (List<User>) criteria.list();
		
		return users;
	}

	public void save(User user) {
		persist(user);
	}
	
	public void update(User user) {
		super.update(user);
	}

	public void deleteByIdu(Integer idu) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("idu", idu));
		User user = (User)crit.uniqueResult();
		delete(user);
	}


}
