package com.example.repository.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import com.example.api.model.WorkspaceModelCustom;
import com.example.repository.CustomRepository;
import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;

@Repository
public class CustomImpl implements CustomRepository {

	@PersistenceContext
	private EntityManager em;

	public Session getSession() {
		return em.unwrap(Session.class);

	}

	public Connection getConnection() {
		Session session = getSession();
		return ((SessionImpl) session).connection();

	}

	@Override
	public List<WorkspaceModelCustom> findWorkspaceTree(String wsName) {

//		StringBuilder sb = new StringBuilder();
//
//		sb.append("WITH RECURSIVE cte AS ( ");
//		sb.append("   SELECT w.id as key, w.name, 1 AS level, null::varchar as parent ");
//		sb.append("   FROM   cl_ms_workspace w ");
//		sb.append("   WHERE  w.workspace_name = :wsName ");
//		sb.append("   UNION  all ");
//		sb.append("   SELECT w.id as key, w.name, c.level + 1 as level, c.name as parent ");
//		sb.append("   FROM   cte      c ");
//		sb.append("   JOIN   cl_ms_workspace w ON w.parent_id = c.key ) ");
//		sb.append("SELECT key, parent, name, level ");
//		sb.append("FROM   cte ");
//		sb.append("ORDER  BY key; ");
		

		
		Session session = getSession();
		
		StoredProcedureQuery query = session.createStoredProcedureCall("list_directory");
		query.registerStoredProcedureParameter(1, Object.class, ParameterMode.REF_CURSOR);
		query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
		query.setParameter(2, wsName);
		
		List<Object[]> listFromDB = query.getResultList();
		List<WorkspaceModelCustom> list = new ArrayList();
		
		for(Object[] res:listFromDB) {
			WorkspaceModelCustom tmp = new WorkspaceModelCustom();
			tmp.setKey((Integer) res[0]);
			tmp.setParent((String) res[1]);
			tmp.setName((String) res[2]);
			tmp.setLevel((Integer) res[3]);
			list.add(tmp);
		}
		
		
//		SQLQuery sqlquery = session.createSQLQuery(sb.toString());
//		
//		sqlquery.setParameter("wsName",wsName);
//		
//		
//		sqlquery.addScalar("key");
//		sqlquery.addScalar("parent");
//		sqlquery.addScalar("name");
//		sqlquery.addScalar("level");
//		
//		sqlquery.setResultTransformer(new FluentHibernateResultTransformer(WorkspaceModelCustom.class));
//		List<WorkspaceModelCustom> list = sqlquery.list();
		
		return list;
	}

}
