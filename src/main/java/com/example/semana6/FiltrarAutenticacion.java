package com.example.semana6;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/html/*", "/control/*" })
public class FiltrarAutenticacion implements Filter {
  
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    HttpSession session = req.getSession(false);

    boolean logueado = (session != null && session.getAttribute("usuario") != null);
    boolean esLogin = req.getRequestURI().endsWith("/login.html");
    boolean esCrearCuenta = req.getRequestURI().endsWith("/crear-cuenta.jsp");
    if (logueado || esLogin || esCrearCuenta) {
      chain.doFilter(request, response);
    } else {
      resp.sendRedirect(req.getContextPath() + "/html/login.html");
    }
  }
}
