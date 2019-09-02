package com.lb.base.service;

import java.util.List;
import java.util.Map;

import com.lb.base.entity.Knowledge;
import com.lb.base.util.Page;

public interface KnowledgeService {
	
	public List<Knowledge> query(Map map, Page page);
	
	public int add(Knowledge knowledge);
	
	public int update(Knowledge knowledge);
	
	public int remove(Long id);
	
	public Knowledge findById(Long id);

	public int queryCount(Map map);
	
}
