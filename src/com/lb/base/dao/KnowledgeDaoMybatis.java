package com.lb.base.dao;

import java.util.List;
import java.util.Map;

import com.framework.orm.mybatis.mapper.MyBatisAnnotation;
import com.lb.base.entity.Knowledge;
import com.lb.base.util.Page;
@MyBatisAnnotation
public interface KnowledgeDaoMybatis {

	
	public List<Knowledge> queryPage(Map<String, Object> entity);
	

	
}
