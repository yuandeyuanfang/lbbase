package com.lb.base.service;

import java.util.List;
import java.util.Map;

import com.lb.base.dao.KnowledgeDao;
import com.lb.base.dao.KnowledgeDaoImpl;
import com.lb.base.entity.Knowledge;
import com.lb.base.util.Page;

public class KnowledgeServiceImpl implements KnowledgeService{
	
	private KnowledgeDao knowledgeDao;
	
	public List<Knowledge> query(Map map, Page page) {
		//setKnowledgeDao();
		return knowledgeDao.query(map,page);
	}
	
	public List<Knowledge> queryPage(Knowledge knowledge, Page page) {
		//setKnowledgeDao();
		return knowledgeDao.queryPage(knowledge,page);
	}

	public int add(Knowledge knowledge) {
		//setKnowledgeDao();
		return knowledgeDao.add(knowledge);
	}

	public int update(Knowledge knowledge) {
		//setKnowledgeDao();
		return knowledgeDao.update(knowledge);
	}

	public int remove(Long id) {
		//setKnowledgeDao();
		return knowledgeDao.remove(id);
	}
	
	public Knowledge findById(Long id){
		//setKnowledgeDao();
		return knowledgeDao.findById(id);
	}

//	private void setKnowledgeDao(){
//		if(knowledgeDao==null){
//			this.knowledgeDao = new KnowledgeDaoImpl();
//		}
//	}
	
	public int queryCount(Map map){
		//setKnowledgeDao();
		return knowledgeDao.queryCount(map);
	}

	public void setKnowledgeDao(KnowledgeDao knowledgeDao) {
		this.knowledgeDao = knowledgeDao;
	}
	
}
