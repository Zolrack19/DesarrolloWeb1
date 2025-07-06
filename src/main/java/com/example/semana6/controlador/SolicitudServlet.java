package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.dto.solicitud.SolicitudCrear;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.facade.SolicitudesFacade;
import com.example.semana6.singleton.ValorDefecto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SolicitudServlet", urlPatterns = {"/control/SolicitudServlet"})
public class SolicitudServlet extends HttpServlet {
  private SolicitudesFacade solicitudesFacade;
  
  @Override
  public void init() throws ServletException {
    solicitudesFacade = new SolicitudesFacade();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    switch (action) {
      case null -> {cargarSolicitudes(req, resp);}
      case "1" -> {
        
      }
      default -> {}
    }

  }

  private void cargarSolicitudes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    int numPag = Integer.parseInt(req.getParameter("numPag"));
    HttpSession session = req.getSession(false);
    Object usuario = (session != null) ? session.getAttribute("usuario") : null;

    List<SolicitudVista> solicitudVistas = null;
    if (numPag > 0) { 
      solicitudVistas = solicitudesFacade.getSolicitudes(usuario, numPag);
    }
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(solicitudVistas);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      short tipoSolicitudId = Short.valueOf(req.getParameter("tipoSolicitudId"));
      String titulo = req.getParameter("titulo");
      String descripcion = req.getParameter("descripcion");
  
      HttpSession session = req.getSession(false);
      Object usuario = (session != null) ? session.getAttribute("usuario") : null;
      SolicitudCrear solicitudCrear = null;
      if (usuario instanceof ClienteDTO) {
        ClienteVista clienteVista = (ClienteVista) usuario;
        solicitudCrear = new SolicitudCrear(tipoSolicitudId, (short) ValorDefecto.ESTADO_SOLICITUD.getValue(), titulo, descripcion, ValorDefecto.VALOR_NULO.getValue(), clienteVista.getId());
      } else if (usuario instanceof ColaboradorDTO) {
        ColaboradorVista colaboradorVista = (ColaboradorVista) usuario;
        if (colaboradorVista.getRolColaborador().equals("Administrador")) {
          short coordinadorId = Short.parseShort(req.getParameter("coordinadorId"));
          short clienteId = Short.parseShort(req.getParameter("clienteId"));
          solicitudCrear = new SolicitudCrear(tipoSolicitudId, (short) ValorDefecto.ESTADO_SOLICITUD.getValue() , titulo, descripcion, coordinadorId, clienteId); // -1 es nulo
        }
      }
  
      SolicitudVista solicitudVista = solicitudesFacade.crearSolicitud(solicitudCrear);
  
      Map<String, Object> json = new HashMap<>();
      json.put("ok", solicitudVista != null);
      json.put("solicitud", solicitudVista);
      new ObjectMapper().writeValue(resp.getWriter(), json);  
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override //PATCH
  public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if ("PATCH".equalsIgnoreCase(req.getMethod())) {
      doPatch(req, resp);
    } else {
      super.service(req, resp);
    }
  }

  // solo para asignar coordinador a la solicitud
  @SuppressWarnings("unchecked")
  protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> campos = mapper.readValue(req.getInputStream(), HashMap.class);
    
    SolicitudVista solicitudVista = solicitudesFacade.asignarCoordinadorASolicitud(campos, usuario);

    Map<String, Object> json = new HashMap<>();
    json.put("ok", solicitudVista != null);
    json.put("solicitud", solicitudVista);

    
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }


  @SuppressWarnings("unchecked")
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    
    HttpSession session = req.getSession(false);
    try {
      ObjectMapper mapper = new ObjectMapper();
      HashMap<String, Object> campos = mapper.readValue(req.getInputStream(), HashMap.class);
      SolicitudVista solicitudVista = solicitudesFacade.actualizarSolicitud(campos, session.getAttribute("usuario"));

      Map<String, Object> json = new HashMap<>();
      json.put("ok", solicitudVista != null);
      json.put("solicitud", solicitudVista);
      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");
      new ObjectMapper().writeValue(resp.getWriter(), json);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    int solicitudId =  Integer.parseInt(req.getParameter("id"));
    solicitudesFacade.eliminarSolicitud(session.getAttribute("usuario"), solicitudId);
  }
}
