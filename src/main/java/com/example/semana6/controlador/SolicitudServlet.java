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
    int idLimite = Integer.parseInt(req.getParameter("idLimite"));
    int maxResultados = Integer.parseInt(req.getParameter("maxResultados"));
    boolean paginaSiguiente = Boolean.parseBoolean(req.getParameter("paginaSiguiente"));

    HttpSession session = req.getSession(false);
    Object usuario = (session != null) ? session.getAttribute("usuario") : null;

    List<SolicitudVista> solicitudVistas = null;

    if (usuario instanceof ClienteDTO) { 
      solicitudVistas = solicitudesFacade.getSolicitudes(((ClienteVista) usuario).getId(), idLimite, maxResultados, paginaSiguiente);
    } else if (usuario instanceof ColaboradorDTO) {
      solicitudVistas = solicitudesFacade.getSolicitudes(idLimite, maxResultados, paginaSiguiente);
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
      short tipoSolicitudId = Short.valueOf(req.getParameter("cbxTipoSolicitud"));
      String titulo = req.getParameter("txtTitulo");
      String descripcion = req.getParameter("txtDescripcion");
  
      HttpSession session = req.getSession(false);
      Object usuario = (session != null) ? session.getAttribute("usuario") : null;
      SolicitudCrear solicitudCrear = null;
      if (usuario instanceof ClienteDTO) {
        ClienteVista clienteVista = (ClienteVista) usuario;
        solicitudCrear = new SolicitudCrear(tipoSolicitudId, (short) ValorDefecto.ESTADO_SOLICITUD.getValue(), titulo, descripcion, ValorDefecto.VALOR_NULO.getValue(), clienteVista.getId());
      } else if (usuario instanceof ColaboradorDTO) {
        ColaboradorVista colaboradorVista = (ColaboradorVista) usuario;
        if (colaboradorVista.getRolColaborador().equals("Administrador")) {
          short estadoSolicitud = Short.valueOf(req.getParameter("cbxEstadoSolicitud"));
          short coordinadorId = Short.valueOf(req.getParameter("coordinadorId"));
          short clienteId = Short.valueOf(req.getParameter("clienteId"));
          solicitudCrear = new SolicitudCrear(tipoSolicitudId, estadoSolicitud, titulo, descripcion, coordinadorId, clienteId); // -1 es nulo
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
}
