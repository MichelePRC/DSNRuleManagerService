package it.uninsubria.rulemanagerservice.springmvc.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.uninsubria.rulemanagerservice.springmvc.model.Resource;
import it.uninsubria.rulemanagerservice.springmvc.model.UploadRequest;



@Repository("uploadRequestDao")
public class UploadRequestDaoImpl extends AbstractDao<Integer, UploadRequest> implements UploadRequestDao {

	public UploadRequest findByToken(Integer token){
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("token", token));
		UploadRequest uploadRequest = (UploadRequest)crit.uniqueResult();
		return uploadRequest;
	}
	
	public void save(UploadRequest uploadRequest) {
		persist(uploadRequest);
	}
	
	
}