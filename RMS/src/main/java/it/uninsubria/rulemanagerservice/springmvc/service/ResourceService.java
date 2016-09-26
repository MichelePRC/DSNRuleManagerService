package it.uninsubria.rulemanagerservice.springmvc.service;

import java.math.BigInteger;
import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.Resource;
import it.uninsubria.rulemanagerservice.springmvc.model.User;


public interface ResourceService {
	
	
	
	Resource findByIdR(Integer idR);
	
	List<Resource> findAllResources();
		
	void saveResource(Resource resource);			

}
