package com.lb.base.listener;

import javax.servlet.ServletContextEvent;

public class ServletContextListener implements javax.servlet.ServletContextListener{

	public void contextInitialized(ServletContextEvent servletcontextevent) {
		System.out.println("ServletContextListener contextInitialized");
	}

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		System.out.println("ServletContextListener contextDestroyed");
	}

}
