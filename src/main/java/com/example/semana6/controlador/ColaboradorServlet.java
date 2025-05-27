package com.example.semana6.controlador;

import java.io.IOException;
import java.util.List;

import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.facade.ColaboradorFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ColaboradorServlet", urlPatterns = { "/control/ColaboradorServlet" })
public class ColaboradorServlet extends HttpServlet {
  private ColaboradorFacade colaboradorFacade = new ColaboradorFacade();


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    List<ColaboradorVista> colaboradoresVista = colaboradorFacade.getcolaboradores(0, 10);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(colaboradoresVista);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
  }
  
}
