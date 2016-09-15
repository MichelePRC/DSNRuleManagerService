package it.uninsubria.rulemanagerservice.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.rulemanagerservice.springmvc.dao.ResourceDao;
import it.uninsubria.rulemanagerservice.springmvc.model.Resource;


@Service("resourceService")
@Transactional
public class ResourceServiceImpl implements ResourceService{

	@Autowired
	private ResourceDao dao;
	

	public Resource findByIdR(Integer idR) {
		Resource resource = dao.findByIdR(idR);
		return resource;
	}
	
	public Resource findByName (String name){
		Resource resource = dao.findByName(name);
		return resource;
	}
	
	
	public Resource findByNameAndIdOwner(String name, Integer id_own){
		Resource resource = dao.findByNameAndIdOwner(name, id_own);
		return resource;
	}
	
	
	public boolean exists (String name, Integer id_owner){
		if (dao.findByNameAndIdOwner(name, id_owner)!=null)
			return true;
		else
			return false;
		
	}
	
	public void deleteByIdR(Integer idR) {
		dao.deleteByIdResource(idR);
	}
	

	public void saveResource(Resource Resource) {
		dao.save(Resource);
	}
		

	public List<Resource> findAllResources() {
		return dao.findAllResources();
	}

		
	
}

