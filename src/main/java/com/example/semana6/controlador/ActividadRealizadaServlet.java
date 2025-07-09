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


  @Override
  public void init() throws ServletException {
    actividadRealizadaF = new ActividadRealizadaF();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();
    json.put("ok", false);

    try {
      do {
        String solicitudId = req.getParameter("solicitudId");
        String colaboradorId = req.getParameter("colaboradorId");

        if (solicitudId == null || !solicitudId.matches("\\d+")) {
          json.put("error", "Id de solicitud inválida: " + solicitudId); break;
        }
        if (colaboradorId == null || !colaboradorId.matches("\\d+")) {
          json.put("error", "Id de colaborador inválida: " + colaboradorId); break;
        }

        List<ActividadRealizadaVista> actividadesVista = actividadRealizadaF.getActividadesByAsignacion(Integer.parseInt(colaboradorId), Integer.parseInt(solicitudId), session.getAttribute("usuario"));
        if (actividadesVista == null) {
          json.put("error", "Error, solicitud no aceptada por el servidor");
        } else {
          json.put("ok", true);
          json.put("actividades", actividadesVista);
        }
      } while (false);
    } catch (Exception e) {
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }

    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();
    json.put("ok", false);

    try {
      do {
        String solicitudId = req.getParameter("solicitudId");
        String colaboradorId = req.getParameter("colaboradorId");
        String horaInicio = req.getParameter("horaInicio");
        String horaFin = req.getParameter("horaFin");
        String descripcion = req.getParameter("descripcion");

        if (horaInicio == null || !horaInicio.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
          json.put("error", "Hora de inicio inválida: " + horaInicio); break;
        }
        if (horaFin == null || !horaFin.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
          json.put("error", "Hora de fin inválida: " + horaFin); break;
        }
        if (LocalTime.parse(horaInicio).isAfter(LocalTime.parse(horaFin))) {
          json.put("error", "La hora de inicio no puede ser mayor a la de fin"); break;
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
          json.put("error", "La descripción no puede estar vacía"); break;
        }
        if (solicitudId == null || !solicitudId.matches("\\d+")) {
          json.put("error", "Id de solicitud inválida: " + solicitudId); break;
        }
        if (colaboradorId == null || !colaboradorId.matches("\\d+")) {
          json.put("error", "Id de colaborador inválida: " + colaboradorId); break;
        }

        ActividadRealizadaVista actividadRealizadaVista = actividadRealizadaF.crearActividad(new ActividadRealizadaCrear(Integer.parseInt(solicitudId), Integer.parseInt(colaboradorId), descripcion, horaInicio, horaFin));
        if (actividadRealizadaVista.getMensajeError() != null) {
          json.put("error", actividadRealizadaVista.getMensajeError());
        } else {
          json.put("ok", true);
          json.put("actividad", actividadRealizadaVista);
        }
      } while (false);
    } catch (Exception e) {
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }

    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
