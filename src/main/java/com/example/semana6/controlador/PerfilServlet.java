package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.semana6.facade.PerfilFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PerfilServlet", urlPatterns = { "/control/PerfilServlet" })
public class PerfilServlet extends HttpServlet {
  private final PerfilFacade perfilFacade = new PerfilFacade();
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  }

  @Override //PATCH
  public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if ("PATCH".equalsIgnoreCase(req.getMethod())) {
      doPatch(req, resp);
    } else {
      super.service(req, resp);
    }
  }

  @SuppressWarnings("unchecked")
  protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> campos = mapper.readValue(req.getInputStream(), HashMap.class);
    boolean exito = perfilFacade.actualizarAtributos(campos, usuario);
    session.setAttribute("usuario", usuario);

    Map<String, Object> json = new HashMap<>();
    json.put("ok", exito);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
    boolean exito = perfilFacade.eliminarPerfil(usuario);
    session.invalidate();

    Map<String, Object> json = new HashMap<>();
    json.put("ok", exito);
    json.put("redirect", req.getContextPath() + "/index.html");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
