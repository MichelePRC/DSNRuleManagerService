package it.uninsubria.rulemanagerservice.springmvc.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.uninsubria.rulemanagerservice.springmvc.model.Resource;
import it.uninsubria.rulemanagerservice.springmvc.model.User;




@Repository("resourceDao")
public class ResourceDaoImpl extends AbstractDao<Integer, Resource> implements ResourceDao {


	public Resource findByIdR(Integer idR) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("idR", idR));
		Resource resource = (Resource)crit.uniqueResult();
		return resource;
	}
	


	public void save(Resource resource) {
		persist(resource);
	}

	public void deleteByIdResource(Integer idR) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("idR", idR));
		Resource resource = (Resource)crit.uniqueResult();
		delete(resource);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Resource> findAllResources() {
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("firstname"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<Resource> resources = (List<Resource>) criteria.list();
		
		return resources;
	}


}
