package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
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
    
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    short tipoSolicitudId = Short.valueOf(req.getParameter("cbxTipoSolicitud"));
    String titulo = req.getParameter("txtTitulo");
    String descripcion = req.getParameter("txtDescripcion");

    HttpSession session = req.getSession(false);
    Object usuario = (session != null) ? session.getAttribute("usuario") : null;
    SolicitudCrear solicitudCrear = null;
    if (usuario instanceof ClienteDTO) {
      ClienteVista clienteVista = (ClienteVista) usuario;
      solicitudCrear = new SolicitudCrear(tipoSolicitudId, ValorDefecto.ESTADO_SOLICITUD.getValue(), titulo, descripcion, ValorDefecto.VALOR_NULO.getValue(), clienteVista.getId());
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
  }
}
