package it.uninsubria.rulemanagerservice.springmvc.dao;

import java.util.List;

import it.uninsubria.rulemanagerservice.springmvc.model.Resource;


public interface ResourceDao {

	Resource findByIdR(Integer idR);
	
	Resource findByName(String name);
	
	Resource findByNameAndIdOwner (String name, Integer id_owner);	
	
	void save(Resource resource);
	
	void deleteByIdResource(Integer id_resource);
	
	List<Resource> findAllResources();	

}
