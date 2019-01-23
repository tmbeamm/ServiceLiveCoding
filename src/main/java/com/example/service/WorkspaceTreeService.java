package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.api.model.WorkspaceModelCustom;
import com.example.api.model.WorkspaceTree;
import com.example.repository.WorkspaceTreeRepository;
import com.example.repository.impl.CustomImpl;

@Service
@Component
public class WorkspaceTreeService {

	@Autowired
	private WorkspaceTreeRepository wsRepo;
	
	@Autowired
	private CustomImpl csRepo;
	
	public List<WorkspaceTree> listData(){
		
		return wsRepo.findAll();
	}
	
	public List<WorkspaceModelCustom> findWSByName(String wsName){
		
		return csRepo.findWorkspaceTree(wsName);
	}
}
