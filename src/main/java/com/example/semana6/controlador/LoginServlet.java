package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.facade.AutenticacionFacade;
import com.example.semana6.modelo.PersonaConNegocio;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
  private AutenticacionFacade autenticacion;

  @Override
  public void init() throws ServletException {
    autenticacion = new AutenticacionFacade();
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    String contrasena = req.getParameter("contrasena");
    Object usuario = autenticacion.iniciarSesion(email, contrasena);

    Map<String, Object> json = new HashMap<>();
    json.put("ok", usuario != null);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");

    if (usuario == null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(resp.getWriter(), json);
      return;

    } else if (usuario instanceof ColaboradorDTO) {
      HttpSession session = req.getSession();
      session.setAttribute("usuario", ((ColaboradorVista) usuario));
      session.setAttribute("rol", "colaborador");
      
    } else if (usuario instanceof ClienteDTO) {
      HttpSession session = req.getSession();
      if (usuario instanceof ClienteVista) {
        session.setAttribute("usuario", ((ClienteVista) usuario));
      } else {
        session.setAttribute("usuario", ((PersonaConNegocio) usuario));
      }
      session.setAttribute("rol", "cliente");
    }

    json.put("redirect", "menu");
    json.put("usuario", usuario);
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(resp.getWriter(), json);
  }
}
