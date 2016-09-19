package it.uninsubria.rulemanagerservice.springmvc.dao;


import it.uninsubria.rulemanagerservice.springmvc.model.UploadRequest;

public interface UploadRequestDao {
	
	UploadRequest findByToken(Integer token);
	
	void save (UploadRequest uploadRequest);
	
	
	

}
