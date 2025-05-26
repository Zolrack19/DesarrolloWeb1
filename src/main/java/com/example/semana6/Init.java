package com.example.semana6;

import com.example.semana6.singleton.HibernateUtil;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Init implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    HibernateUtil.getSession();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    HibernateUtil.getSession().close();
  }
}
