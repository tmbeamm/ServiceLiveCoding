package com.example.repository;

import java.util.List;

import com.example.api.model.WorkspaceModelCustom;

public interface CustomRepository {
	List<WorkspaceModelCustom> findWorkspaceTree(String wsName);

}
