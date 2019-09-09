package com.lb.base.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lb.base.entity.DataDictionary;
import com.lb.base.entity.Knowledge;
import com.lb.base.service.KnowledgeService;
import com.lb.base.service.KnowledgeServiceImpl;
import com.lb.base.util.Page;

public class KnowledgeAction {
	
	private KnowledgeService knowledgeService;
	
	public void query(HttpServletRequest req, HttpServletResponse response) throws Exception{
		//setKnowledgeService();获取service实例，不使用spring时用到
		Map map = new HashMap();
		Page page = Page.getPage(req);
		page.setTotalRecord(knowledgeService.queryCount(map));
		page.setActionUrl(req.getContextPath()+"/knowledge/query.action");
		List<Knowledge> knowledgeList = knowledgeService.query(map,page);
		req.setAttribute("knowledgeList", knowledgeList);
		req.setAttribute("pageHtml", page.getPageHelper());
		String returnUrl = "/knowledge/knowledgeQuery.jsp";
		req.getRequestDispatcher(returnUrl).forward(req, response);//转发方式可以访问WEB-INF文件夹下的页面
	}
	
	public void addGet(HttpServletRequest req, HttpServletResponse response) throws Exception{
		List<DataDictionary> dataDictionaryList = new ArrayList<DataDictionary>();
		DataDictionary dataDictionary = new DataDictionary();
		dataDictionary.setId(1);
		dataDictionary.setDataTypesCode("1");
		dataDictionary.setDataTypesName("知识点类型");
		dataDictionary.setDataTypeCode("1");
		dataDictionary.setDataTypeName("JAVA");
		dataDictionaryList.add(dataDictionary);
		DataDictionary dataDictionary2 = new DataDictionary();
		dataDictionary2.setId(2);
		dataDictionary2.setDataTypesCode("1");
		dataDictionary2.setDataTypesName("知识点类型");
		dataDictionary2.setDataTypeCode("2");
		dataDictionary2.setDataTypeName("WEB知识");
		dataDictionaryList.add(dataDictionary2);
		req.setAttribute("dataDictionaryList", dataDictionaryList);
		String returnUrl = "/knowledge/knowledgeAdd.jsp";
		req.getRequestDispatcher(returnUrl).forward(req, response);
	}
	
	public void addPost(HttpServletRequest req, HttpServletResponse response) throws Exception{
		//setKnowledgeService();获取service实例，不使用spring时用到
		Knowledge knowledge = new Knowledge();
		knowledge = knowledge.getKnowledge(req);
		if(knowledgeService.add(knowledge)!=1){
			throw new Exception();
		}
		response.sendRedirect(req.getContextPath()+"/knowledge/query.action");//重定向方式不能访问WEB-INF文件夹下的页面
	}
	
	public void updateGet(HttpServletRequest req, HttpServletResponse response) throws Exception{
		//setKnowledgeService();获取service实例，不使用spring时用到
		Knowledge knowledge = knowledgeService.findById(Long.parseLong(req.getParameter("id")));
		req.setAttribute("knowledge", knowledge);
		List<DataDictionary> dataDictionaryList = new ArrayList<DataDictionary>();
		DataDictionary dataDictionary = new DataDictionary();
		dataDictionary.setId(1);
		dataDictionary.setDataTypesCode("1");
		dataDictionary.setDataTypesName("知识点类型");
		dataDictionary.setDataTypeCode("1");
		dataDictionary.setDataTypeName("JAVA");
		dataDictionaryList.add(dataDictionary);
		DataDictionary dataDictionary2 = new DataDictionary();
		dataDictionary2.setId(2);
		dataDictionary2.setDataTypesCode("1");
		dataDictionary2.setDataTypesName("知识点类型");
		dataDictionary2.setDataTypeCode("2");
		dataDictionary2.setDataTypeName("WEB知识");
		dataDictionaryList.add(dataDictionary2);
		req.setAttribute("dataDictionaryList", dataDictionaryList);
		String returnUrl = "/knowledge/knowledgeUpdate.jsp";
		req.getRequestDispatcher(returnUrl).forward(req, response);
	}
	
	public void updatePost(HttpServletRequest req, HttpServletResponse response) throws Exception{
		//setKnowledgeService();获取service实例，不使用spring时用到
		Knowledge knowledge = new Knowledge();
		knowledge = knowledge.getKnowledge(req);
		if(knowledgeService.update(knowledge)!=1){
			throw new Exception();
		}
		response.sendRedirect(req.getContextPath()+"/knowledge/query.action");
	}
	
	public void remove(HttpServletRequest req, HttpServletResponse response) throws Exception{
		//setKnowledgeService();获取service实例，不使用spring时用到
		if(knowledgeService.remove(Long.parseLong(req.getParameter("id")))!=1){
			throw new Exception();
		}
		response.sendRedirect(req.getContextPath()+"/knowledge/query.action");
	}

	//spring绑定依赖关系
	public void setKnowledgeService(KnowledgeService knowledgeService) {
		this.knowledgeService = knowledgeService;
	}
	
	/**
	 * 获取service实例，不使用spring时用到
	 */
//	private void setKnowledgeService(){
//		if(knowledgeService==null){
//			this.knowledgeService = new KnowledgeServiceImpl();
//		}
//	}
	
}
