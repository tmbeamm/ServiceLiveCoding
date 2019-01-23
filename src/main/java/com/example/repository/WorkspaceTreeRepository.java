package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.model.WorkspaceTree;

@Repository
public interface WorkspaceTreeRepository extends JpaRepository<WorkspaceTree, Integer>{
	
	public List<WorkspaceTree> findAll();
	

}
