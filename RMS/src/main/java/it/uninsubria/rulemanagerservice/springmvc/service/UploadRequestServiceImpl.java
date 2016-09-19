package it.uninsubria.rulemanagerservice.springmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uninsubria.rulemanagerservice.springmvc.dao.UploadRequestDao;
import it.uninsubria.rulemanagerservice.springmvc.model.UploadRequest;

@Service("uploadRequestService")
@Transactional
public class UploadRequestServiceImpl implements UploadRequestService {
	
	@Autowired
	private UploadRequestDao dao;
	
	
	public UploadRequest findByToken(Integer token){
		
		return dao.findByToken(token);	
	}
	
	public void save (UploadRequest uploadRequest){
		
		dao.save(uploadRequest);
	}
	
	

}
