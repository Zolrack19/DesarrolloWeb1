package com.example.semana6.controlador;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.dto.solicitud.SolicitudCrudo;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.facade.SolicitudesFacade;
import com.example.semana6.singleton.ValorDefecto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    
    Map<String, Object> json = new HashMap<>();
    json.put("ok", false);

    String action = req.getParameter("action");
    try {
      switch (action) {
        case null -> {cargarSolicitudes(req, resp, json);}
        case "1" -> {
          solicitudesDeColaborador(req, resp, json);
        }
        case "2" -> {
          solicitudesDeCliente(req, resp, json);
        }
        case "3" -> {
          solicitudesRagoFechasPorEntidad(req, resp, json); //cliente, colaborador o solicitudes solas
        }
        default -> {
        }
      }
    } catch (Exception e) {
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.writeValue(resp.getWriter(), json);
  }


  private void solicitudesRagoFechasPorEntidad(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> json) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
      
    salir:
    do {
      String mes = req.getParameter("mes");
      String anio = req.getParameter("anio");
      String tipoBusqueda = req.getParameter("tipoBusqueda");
      
      if (mes == null || !mes.matches("\\d+")) {
        json.put("error", "Número de mes no válido: " + mes); break;
      }
      if (anio == null || !anio.matches("\\d+")) {
        json.put("error", "Número de año no válido: " + anio); break;
      }
      LocalDateTime fechaInicio = LocalDateTime.of(Integer.parseInt(anio), Integer.parseInt(mes), 1, 0, 0);
      LocalDateTime fechaFin = fechaInicio.plusMonths(1);
      List<SolicitudCrudo> solicitudCrudos = null;
      
      switch (tipoBusqueda) {
        case "-1" -> { //solicitud
          solicitudCrudos = solicitudesFacade.getSolicitudes(usuario, fechaInicio, fechaFin);
        } 
        case "1" -> { //colaborador
          String entId = req.getParameter("entidadId");
          if (entId == null || !entId.matches("\\d+")) {
            json.put("error", "Id de colaborador no válido: " + entId);
            break salir;
          }
          solicitudCrudos = solicitudesFacade.getSolicitudesByColaborador(usuario, Integer.parseInt(entId), fechaInicio, fechaFin);
        } 
        case "2" -> { //cliente
          String entId = req.getParameter("entidadId");
          if (entId == null || !entId.matches("\\d+")) {
            json.put("error", "Id de cliente no válido: " + entId);
            break salir;
          }
          solicitudCrudos = solicitudesFacade.getSolicitudesByCliente(usuario, Integer.parseInt(entId), fechaInicio, fechaFin);
        } 
        default -> { //lanzar error
        }
      }
      json.put("ok", true);
      json.put("solicitudes", solicitudCrudos);
    } while (false);
  }

  private void solicitudesDeCliente(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> json) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
    
    do {
      if (usuario == null) {
        json.put("error", "No hay sesión activa, no puede ejecutar ninguna operación hasta volver a inicar sesión"); break;
      }
      String numP = req.getParameter("numPag");
      String clientId = req.getParameter("clienteId");

      if (numP == null || !numP.matches("\\d+")) {
        json.put("error", "Número de página debe ser de tipo entero: " + numP); break;
      }
      if (clientId == null || !clientId.matches("\\d+")) {
        json.put("error", "Id de cliente no es válida: " + clientId); break;
      }

      int numPag = Integer.parseInt(numP);
      if (numPag < 0) {
        json.put("error", "Número de página no puede ser un negativo: " + numP); break;
      }
      int clienteId = Integer.parseInt(clientId);

      List<SolicitudVista> solicitudVistas = solicitudesFacade.getSolicitudesByCliente(usuario, clienteId, numPag);
      
      json.put("ok", true);
      json.put("solicitudes", solicitudVistas);
    } while (false);
  }

  private void solicitudesDeColaborador(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> json) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
    
    do {
      if (usuario == null) {
        json.put("error", "No hay sesión activa, no puedes ejecutar ninguna operación hasta volver a inicar sesión"); break;
      }
      String numP = req.getParameter("numPag");
      String colabId = req.getParameter("colaboradorId");

      if (numP == null || !numP.matches("\\d+")) {
        json.put("error", "Número de página debe ser de tipo entero: " + numP); break;
      }
      if (colabId == null || !colabId.matches("\\d+")) {
        json.put("error", "Id de colaborador no es válida: " + colabId); break;
      }

      int numPag = Integer.parseInt(numP);
      if (numPag < 0) {
        json.put("error", "Número de página no puede ser un negativo: " + numP); break;
      }
      int colaboradorId = Integer.parseInt(colabId);

      List<SolicitudVista> solicitudVistas = solicitudesFacade.getSolicitudesByColaborador(usuario, colaboradorId, numPag);
      
      json.put("ok", true);
      json.put("solicitudes", solicitudVistas);
    } while (false);
  }

  private void cargarSolicitudes(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> json) throws ServletException, IOException {
    int numPag = Integer.parseInt(req.getParameter("numPag"));
    HttpSession session = req.getSession(false);
    Object usuario = (session != null) ? session.getAttribute("usuario") : null;

    List<SolicitudVista> solicitudVistas = null;
    if (numPag > 0) { 
      solicitudVistas = solicitudesFacade.getSolicitudes(usuario, numPag);
    }
    
    json.put("ok", solicitudVistas != null);
    json.put("solicitudes", solicitudVistas);

    new ObjectMapper().writeValue(resp.getWriter(), json);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");

    Map<String, Object> json = new HashMap<>();
    json.put("ok", false);

    try {
      do {
        if (usuario == null) {
          json.put("error", "No hay sesión activa, no puedes ejecutar ninguna operación hasta volver a inicar sesión"); break;
        }

        String tSoli = req.getParameter("tipoSolicitudId");
        String titulo = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        
        if (tSoli == null || !tSoli.matches("\\d+")) {
          json.put("error", "Tipo de solicitud no válido: " + tSoli); break;
        }
        if (titulo == null) {
          json.put("error", "Titulo inválido, no puede ser nulo"); break;
        }
        if (descripcion == null) {
          json.put("error", "Descripción inválida, no puede ser nulo"); break;
        }
        titulo = titulo.trim();
        descripcion = descripcion.trim();
        if (titulo.isEmpty() || titulo.length() > 200) {
          json.put("error", "Título inválido (debe tener como máximo 200 caracteres)"); break;
        }
        if (descripcion.isEmpty()) {
          json.put("error", "Descripción inválida, no puede ser vacio"); break;
        }

        short tipoSolicitudId = Short.valueOf(tSoli);
        SolicitudCrudo solicitudCrear = null;
        if (usuario instanceof ClienteDTO) {
          ClienteVista clienteVista = (ClienteVista) usuario;
          solicitudCrear = new SolicitudCrudo(tipoSolicitudId, (short) ValorDefecto.ESTADO_SOLICITUD.getValue(), titulo, descripcion, ValorDefecto.VALOR_NULO.getValue(), clienteVista.getId());
        } else if (usuario instanceof ColaboradorDTO) {
          ColaboradorVista colaboradorVista = (ColaboradorVista) usuario;
          if (colaboradorVista.getRolColaborador().equals("Administrador")) {
            short coordinadorId = Short.parseShort(req.getParameter("coordinadorId"));
            short clienteId = Short.parseShort(req.getParameter("clienteId"));
            solicitudCrear = new SolicitudCrudo(tipoSolicitudId, (short) ValorDefecto.ESTADO_SOLICITUD.getValue() , titulo, descripcion, coordinadorId, clienteId); // -1 es nulo
          }
        }
        SolicitudVista solicitudVista = solicitudesFacade.crearSolicitud(solicitudCrear);
    
        json.put("ok", solicitudVista != null);
        json.put("solicitud", solicitudVista);
      } while (false);
    } catch (Exception e) {
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }

    new ObjectMapper().writeValue(resp.getWriter(), json);  
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
