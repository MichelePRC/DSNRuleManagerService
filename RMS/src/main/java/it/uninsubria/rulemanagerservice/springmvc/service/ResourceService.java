package it.uninsubria.rulemanagerservice.springmvc.service;

import java.math.BigInteger;
import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.Resource;


public interface ResourceService {
	
	
	
	Resource findByIdR(Integer idR);
	
	Resource findByName(String name);
	
	Resource findByNameAndIdOwner(String name, Integer id_own);
	
	boolean exists (String name, Integer id_owner);
	
	void deleteByIdR(Integer idR);
	
	List<Resource> findAllResources();
		
	void saveResource(Resource resource);			

}
