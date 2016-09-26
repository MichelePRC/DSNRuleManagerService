package it.uninsubria.rulemanagerservice.springmvc.dao;

import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.Resource;
import it.uninsubria.rulemanagerservice.springmvc.model.User;


public interface ResourceDao {

	Resource findByIdR(Integer idR);
	
	void save(Resource resource);
	
	List<Resource> findAllResources();	

}
