package com.lb.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.lb.base.dao.KnowledgeDao;
import com.lb.base.dao.KnowledgeDaoMybatis;
import com.lb.base.entity.Knowledge;
import com.lb.base.util.Page;

public class KnowledgeServiceImpl implements KnowledgeService{
	
	private KnowledgeDao knowledgeDao;
	//@Autowired
	@Resource
	private KnowledgeDaoMybatis knowledgeDaoMybatis;
	
	public List<Knowledge> query(Map map, Page page) {
		//setKnowledgeDao();
		return knowledgeDao.query(map,page);
	}
	
	public List<Knowledge> queryPage(Knowledge knowledge, Page page) {
		//setKnowledgeDao();
//		return knowledgeDao.queryPage(knowledge,page);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("knowledge", knowledge);
		map.put("page", page);
		return knowledgeDaoMybatis.queryPage(map);
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
//		return knowledgeDao.queryCount(map);
		return 5;
	}

	public void setKnowledgeDao(KnowledgeDao knowledgeDao) {
		this.knowledgeDao = knowledgeDao;
	}
	
}
