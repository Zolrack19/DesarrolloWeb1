package com.example.semana6.controlador;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semana6.dto.actividadRealizada.ActividadRealizadaCrear;
import com.example.semana6.dto.actividadRealizada.ActividadRealizadaVista;
import com.example.semana6.facade.ActividadRealizadaF;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ActividadRealizadaServlet", urlPatterns = {"/control/ActividadRealizadaServlet"})
public class ActividadRealizadaServlet extends HttpServlet {
  
  private ActividadRealizadaF actividadRealizadaF;
  private StringBuilder mensajeError;


  @Override
  public void init() throws ServletException {
    actividadRealizadaF = new ActividadRealizadaF();
    mensajeError = new StringBuilder();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();

    try {
      for (int i = 0; i < 1; i++) {
        String solicitudId = req.getParameter("solicitudId");
        String colaboradorId = req.getParameter("colaboradorId");

        if (solicitudId == null || !solicitudId.matches("\\d+")) {
          mensajeError.append("\nLa id: ").append(solicitudId).append(" de solicitud es inválida");
        }
        if (colaboradorId == null || !colaboradorId.matches("\\d+")) {
          mensajeError.append("\nId: ").append(colaboradorId).append(" de colaborador es inválida");
        }
        if (!mensajeError.isEmpty()) break;

        List<ActividadRealizadaVista> actividadesVista = actividadRealizadaF.getActividadesByAsignacion(Integer.parseInt(colaboradorId), Integer.parseInt(solicitudId), session.getAttribute("usuario"));
        if (actividadesVista == null) {
          json.put("ok", false);
          json.put("error", "Error, solicitud no aceptada por el servidor");
        } else {
          json.put("ok", true);
          json.put("actividades", actividadesVista);
        }
      }
    } catch (Exception e) {
      json.put("ok", false);
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }


    if (!mensajeError.isEmpty()) {
      json.put("ok", false);
      json.put("error", mensajeError.toString());
    }
    
    mensajeError.setLength(0);
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();

    try {
      for (int i = 0; i < 1; i++) {
        String solicitudId = req.getParameter("solicitudId");
        String colaboradorId = req.getParameter("colaboradorId");
        String horaInicio = req.getParameter("horaInicio");
        String horaFin = req.getParameter("horaFin");
        String descripcion = req.getParameter("descripcion");

        if (horaInicio == null || !horaInicio.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
          mensajeError.append("Hora de inicio inválida: ").append(horaInicio);
          break;
        }
        if (horaFin == null || !horaFin.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
          mensajeError.append("Hora de fin inválida: ").append(horaFin);
          break;
        }
        if (LocalTime.parse(horaInicio).isAfter(LocalTime.parse(horaFin))) {
          mensajeError.append("La hora de inicio no puede ser mayor a la de fin");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
          mensajeError.append("\nLa descripción no puede estar vacía");
        }
        if (solicitudId == null || !solicitudId.matches("\\d+")) {
          mensajeError.append("\nLa id: ").append(solicitudId).append(" de solicitud es inválida");
        }
        if (colaboradorId == null || !colaboradorId.matches("\\d+")) {
          mensajeError.append("\nId: ").append(colaboradorId).append(" de colaborador es inválida");
        }

        if (!mensajeError.isEmpty()) break;

        ActividadRealizadaVista actividadRealizadaVista = actividadRealizadaF.crearActividad(new ActividadRealizadaCrear(Integer.parseInt(solicitudId), Integer.parseInt(colaboradorId), descripcion, horaInicio, horaFin));
        if (actividadRealizadaVista.getMensajeError() != null) {
          json.put("ok", false);
          json.put("error", actividadRealizadaVista.getMensajeError());
        } else {
          json.put("ok", true);
          json.put("actividad", actividadRealizadaVista);
        }
      }
    } catch (Exception e) {
      json.put("ok", false);
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }

    if (!mensajeError.isEmpty()) {
      json.put("ok", false);
      json.put("error", mensajeError.toString());
    }
    
    mensajeError.setLength(0);
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
