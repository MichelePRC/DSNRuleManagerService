package it.uninsubria.rulemanagerservice.springmvc.dao;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao<PK extends Serializable, T> {
	
	private final Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDao(){
		this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired 
	public void setSession(SessionFactory sess){
		this.sessionFactory=sess;
	}
	
	
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T getByKey(BigInteger id) {
		return (T) getSession().get(persistentClass, id);
	}

	public void persist(T entity) {
		getSession().persist(entity);
	}
	
	public void update(T entity) {
		getSession().saveOrUpdate(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(persistentClass);
	}

}
