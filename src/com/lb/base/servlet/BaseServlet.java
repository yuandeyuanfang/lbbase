package com.lb.base.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lb.base.controller.KnowledgeAction;

public class BaseServlet extends HttpServlet{

	/**
	 * 处理所有请求，根据不同请求调用对应Action处理
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(this.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse response) {
		logger.info("BaseServlet doGet");
		service(req,response);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse response) {
		logger.info("BaseServlet doPost");
		service(req,response);
	}
	
	public void service(HttpServletRequest req, HttpServletResponse response) {
		logger.info("BaseServlet service");
		String url = pareRequestURI(req);
		try {
			if(url.equals("/invalidPage.jsp") || url.equals("/errorPage.jsp")){
			}else if(url.startsWith("knowledge/")){
				url =  url.replaceFirst("knowledge/", "");
				if(url.equals("query.action") || url.startsWith("query.action?") ){
					setKnowledgeAction();
					knowledgeAction.query(req, response);
				}else if(url.equals("add.action") || url.startsWith("add.action?") ){
					setKnowledgeAction();
					if(req.getMethod().equals("GET")){
						knowledgeAction.addGet(req, response);
					}else if (req.getMethod().equals("POST")){
						knowledgeAction.addPost(req, response);
					}
				}else if(url.equals("update.action") || url.startsWith("update.action?") ){
					setKnowledgeAction();
					if(req.getMethod().equals("GET")){
						knowledgeAction.updateGet(req, response);
					}else if (req.getMethod().equals("POST")){
						knowledgeAction.updatePost(req, response);
					}
				}else if(url.equals("remove.action") || url.startsWith("remove.action?") ){
					setKnowledgeAction();
					knowledgeAction.remove(req, response);
				}else {
					String returnUrl = "/invalidPage.jsp";
					try {
						req.getRequestDispatcher(returnUrl).forward(req, response);
					} catch (ServletException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				String returnUrl = "/invalidPage.jsp";//请求地址无效，跳转到无效地址页面
				try {
					req.getRequestDispatcher(returnUrl).forward(req, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			logger.error("Error Stack: ",e);
			String returnUrl = "/errorPage.jsp";//程序出错，跳转到错误页面
			try {
				req.getRequestDispatcher(returnUrl).forward(req, response);
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取除去项目根路径后的url
	 * @param request
	 * @return
	 */
	private String pareRequestURI(HttpServletRequest request) {
		String path = request.getContextPath() + "/";
		String requestUri = request.getRequestURI();
		String midUrl = requestUri.replaceFirst(path, "");
		return midUrl;
	}
	
	//根据不同请求调用相应action里面的方法
	private KnowledgeAction knowledgeAction;
	private void setKnowledgeAction(){
		if(knowledgeAction == null){
			knowledgeAction = new KnowledgeAction();
		}
	}
}
