package com.lb.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class Knowledge implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String menu;
	private String pageLink;
	private String javaPath;
	private Long pointType;
	private Date createTime;
	
	private byte[] picture;//Blob类型字段
	private String article;//Clob类型字段
	
	//不对应数据库
	private String pointTypeStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}

	public String getJavaPath() {
		return javaPath;
	}

	public void setJavaPath(String javaPath) {
		this.javaPath = javaPath;
	}

	public Long getPointType() {
		return pointType;
	}

	public void setPointType(Long pointType) {
		this.pointType = pointType;
	}

	public String getPointTypeStr() {
		return pointTypeStr;
	}

	public void setPointTypeStr(String pointTypeStr) {
		this.pointTypeStr = pointTypeStr;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Knowledge getKnowledge(HttpServletRequest req){
		Knowledge knowledge = new Knowledge();
		if(req.getParameter("id")!=null && req.getParameter("id").length()>0){
			knowledge.setId(Long.parseLong(req.getParameter("id")));
		}
		if(req.getParameter("name")!=null && req.getParameter("name").length()>0){
			knowledge.setName( req.getParameter("name"));
		}
		if(req.getParameter("menu")!=null && req.getParameter("menu").length()>0){
			knowledge.setMenu( req.getParameter("menu"));
		}
		if(req.getParameter("pageLink")!=null && req.getParameter("pageLink").length()>0){
			knowledge.setPageLink( req.getParameter("pageLink"));
		}
		if(req.getParameter("javaPath")!=null && req.getParameter("javaPath").length()>0){
			knowledge.setJavaPath( req.getParameter("javaPath"));
		}
		if(req.getParameter("pointType")!=null && req.getParameter("pointType").length()>0){
			knowledge.setPointType(Long.parseLong(req.getParameter("pointType")));
		}
		if(req.getParameter("picture")!=null && req.getParameter("picture").length()>0){
			knowledge.setPicture(req.getParameter("picture").getBytes());
		}
		if(req.getParameter("article")!=null && req.getParameter("article").length()>0){
			knowledge.setArticle( req.getParameter("article"));
		}
		return knowledge;
	}
	
}
