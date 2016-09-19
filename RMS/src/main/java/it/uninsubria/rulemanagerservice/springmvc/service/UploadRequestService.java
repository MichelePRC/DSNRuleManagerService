package it.uninsubria.rulemanagerservice.springmvc.service;

import it.uninsubria.rulemanagerservice.springmvc.model.UploadRequest;

public interface UploadRequestService {

	UploadRequest findByToken(Integer token);
	
	void save (UploadRequest uploadRequest);
	
	
}
