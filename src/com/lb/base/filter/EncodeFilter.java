package com.lb.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodeFilter implements Filter{

	private FilterConfig config;
	private String encoding = "ISO-8859-1";
	
	public void init(FilterConfig filterconfig) throws ServletException {
		System.out.println("EncodeFilter init");
		this.config = filterconfig;
		String param = config.getInitParameter("encoding");
		if(param!=null){
			encoding = param;
		}
	}

	public void doFilter(ServletRequest servletrequest,
			ServletResponse servletresponse, FilterChain filterchain)
			throws IOException, ServletException {
		System.out.println("EncodeFilter doFilter");
		servletrequest.setCharacterEncoding(encoding);
		servletresponse.setCharacterEncoding(encoding);
		servletresponse.setContentType("text/html;charset="+encoding);
		filterchain.doFilter(servletrequest, servletresponse);
	}

	public void destroy() {
		System.out.println("EncodeFilter destory");
		config = null;
	}

}
