package com.example.semana6.controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MenuServlet", urlPatterns = { "/html/menu/*" })
public class MenuServlet extends HttpServlet {
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("Hola");
    System.out.println(req.getRequestURI());
    req.getRequestDispatcher("/html/menu.jsp").forward(req, resp);
  }
}