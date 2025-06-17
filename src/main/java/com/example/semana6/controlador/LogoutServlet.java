package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = ("/control/LogoutServlet"))
public class LogoutServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    Map<String, Object> json = new HashMap<>();
    json.put("redirect", req.getContextPath() + "/html/login.html");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
