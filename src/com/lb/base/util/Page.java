package com.lb.base.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class Page implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int curPage=1;
	private int pageSize=10;
	private int totalRecord;
	private int totalPage;
	private String actionUrl;//页面Url
	private String firstImgUrl="/image/pageFirst.png";//跳转到第一页图标地址
	private String lastImgUrl="/image/pageLast.png";//跳转到最后一页图标地址
	private String nImgUrl="/image/pageN.png";//跳转到第n页图标地址
	
	private Boolean isToBottom;	//查询从当前分页开始到最后一条
	
	
	/**
	 * 获取rowNum开始
	 * @return
	 */
	public int getBegin(){
		return (getCurPage() - 1)*pageSize;
	}
	
	/**
	 * 获取rowNum结束
	 * @return
	 */
	public int getEnd(){
		if(isToBottom!=null && isToBottom)
			return totalRecord+1;
		return getCurPage()*pageSize +1;
	}
	
	/**
	 * 获取当前页数
	 * @return
	 */
	public int getCurPage() {
		if(curPage>totalPage && totalPage>0){
			curPage = totalPage;
		}
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * 获取总数量
	 * @return
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		if(totalRecord>0){
			totalPage = totalRecord/pageSize;
			if(totalRecord%pageSize>0){
				totalPage++;
			}
		}
		this.totalRecord = totalRecord;
	}

	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	
	public Boolean getIsToBottom() {
		return isToBottom;
	}
	public void setIsToBottom(Boolean isToBottom) {
		this.isToBottom = isToBottom;
	}
	
	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPage() {
		return totalPage;
	}
	
	public int getNextPages(){
		int nextRecord = totalRecord - (curPage * pageSize);
		if(nextRecord<=0)	return 0;
		return (nextRecord / pageSize) + 1;
	}
	
	public int getNextRecords(){
		int nextRecord = totalRecord - (curPage * pageSize);
		if(nextRecord<=0) return 0;
		return nextRecord;
	}
		
	public String getPageHelper(){
			StringBuffer pageHelper = new StringBuffer();
			pageHelper.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"3\">");
			pageHelper.append("    <tr>");
			pageHelper.append("        <td>共" + totalRecord + "行，" + getTotalPage() + "页 ");
			if(totalPage>5&&getCurPage()>3){
				//跳转到第一页的代码
				pageHelper.append("     &nbsp;&nbsp;<a href='javascript:goPage(1)'><img src=\"" + firstImgUrl +"\" width=\"16\" height=\"13\" border=\"0\" align=\"center\"></a>");
			}
			if(totalPage<6){
				for(int i=1;i<=totalPage;i++){
					if(i==getCurPage()){
						pageHelper.append(" &nbsp;&nbsp;<b>" + i + "</b>");
					}
					else{
						pageHelper.append(" &nbsp;&nbsp;<a href=\"javascript:goPage(" + i + ")\">" + i + "</a>");
					}
				}
			}
			else{
				if(curPage<4){
					for(int i=1;i<6;i++){
						if(i==curPage){
							pageHelper.append(" &nbsp;&nbsp;<b>" + i + "</b>");
						}
						else{
							pageHelper.append(" &nbsp;&nbsp;<a href=\"javascript:goPage(" + i + ")\">" + i + "</a>");
						}
					}
				}
				else{
					if(totalPage-curPage>1){
						for(int i=1;i<6;i++){
							if(i==3){
								pageHelper.append(" &nbsp;&nbsp;<b>" + curPage + "</b>");
							}
							else{
								pageHelper.append(" &nbsp;&nbsp;<a href=\"javascript:goPage(" + (i+curPage-3) + ")\">" + (i+curPage-3) + "</a>");
							}
						}
					}
					else{
						for(int i=totalPage-4;i<=totalPage;i++){
							if(i==getCurPage()){
								pageHelper.append(" &nbsp;&nbsp;<b>" + i + "</b>");
							}
							else{
								pageHelper.append(" &nbsp;&nbsp;<a href=\"javascript:goPage(" + i + ")\">" + i + "</a>");
							}
						}
					}
				}
			}
			if(totalPage>5&&totalPage-curPage>2){
				//跳转到最后一页的代码
				pageHelper.append(" &nbsp;&nbsp;<a href='javascript:goPage(" + totalPage + ")'><img src=\"" + lastImgUrl + "\" width=\"16\" height=\"13\" border=\"0\" align=\"center\"></a>");
			}
			if(totalPage>0){
				//跳转到N页的代码
				pageHelper.append(" &nbsp;&nbsp;<input type=\"text\" id=\"goPage\" style=\"width:30px\" /> 页<a href=\"javascript:goPage()\"><img src=\"" + nImgUrl + "\" width=\"15\" height=\"15\" border=\"0\" align=\"center\"/></a>");
			}
			pageHelper.append("		   <input type=\"hidden\" id=\"actionUrl\" value=\"" + actionUrl + "\"\\>");
			pageHelper.append("		   <input type=\"hidden\" id=\"pageSize\" value=\"" + pageSize + "\"\\>");
			pageHelper.append("		   <input type=\"hidden\" id=\"curPage\" name=\"curPage\" value=\"" + curPage + "\"\\>");
			pageHelper.append("        </td>");
			pageHelper.append("    </tr>");
			pageHelper.append("</table>");		
			
			return pageHelper.toString();
	}
	
	public static Page getPage(HttpServletRequest req){
		Page page = new Page();
		page.setFirstImgUrl(req.getContextPath()+page.firstImgUrl);
		page.setLastImgUrl(req.getContextPath()+page.lastImgUrl);
		page.setnImgUrl(req.getContextPath()+page.nImgUrl);
		if(req.getParameter("curPage")!=null && req.getParameter("curPage").length()>0){
			page.setCurPage(Integer.parseInt(req.getParameter("curPage")));
		}
		return page;
	}

	public String getFirstImgUrl() {
		return firstImgUrl;
	}

	public void setFirstImgUrl(String firstImgUrl) {
		this.firstImgUrl = firstImgUrl;
	}

	public String getLastImgUrl() {
		return lastImgUrl;
	}

	public void setLastImgUrl(String lastImgUrl) {
		this.lastImgUrl = lastImgUrl;
	}

	public String getnImgUrl() {
		return nImgUrl;
	}

	public void setnImgUrl(String nImgUrl) {
		this.nImgUrl = nImgUrl;
	}
	
}
