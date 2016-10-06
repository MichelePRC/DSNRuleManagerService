package it.uninsubria.rulemanagerservice.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.rulemanagerservice.springmvc.dao.ResourceDao;
import it.uninsubria.rulemanagerservice.springmvc.model.Resource;
import it.uninsubria.rulemanagerservice.springmvc.model.User;


@Service("resourceService")
@Transactional
public class ResourceServiceImpl implements ResourceService{

	@Autowired
	private ResourceDao dao;
	

	public Resource findByIdR(Integer idR) {
		Resource resource = dao.findByIdR(idR);
		return resource;
	}
			
	public void update (Resource resource){
		dao.update(resource);
	}

	public void saveResource(Resource Resource) {
		dao.save(Resource);
	}
		

	public List<Resource> findAllResources() {
		return dao.findAllResources();
	}

		
	
}

