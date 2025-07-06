package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.facade.AsignacionFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AsignacionServlet", urlPatterns = { "/control/AsignacionServlet" })
public class AsignacionServlet extends HttpServlet {

  private AsignacionFacade asignacionFacade;
  private StringBuilder mensajeError;

  @Override
  public void init() throws ServletException {
    asignacionFacade = new AsignacionFacade();
    mensajeError = new StringBuilder();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");

    Map<String, Object> json = new HashMap<>();

    try {
      while (true) {
        String solicId = req.getParameter("solicitudId");
        String colabId = req.getParameter("colaboradorId");
        if (solicId == null || !solicId.matches("\\d+")) {
          mensajeError.append("\nId de solicitud no válido: ").append(solicId);
        }
        if (colabId == null || !colabId.matches("\\d+")) {
          mensajeError.append("\nId de colaborador no válido: ").append(colabId);
        }

        if (!mensajeError.isEmpty()) break;

        int solicitudId = Integer.parseInt(solicId);
        int colaboradorId = Integer.parseInt(colabId);
        ColaboradorVista colaboradorVista = asignacionFacade.crearAsignacion(solicitudId, colaboradorId);
        if (colaboradorVista.getMensajeError() != null) {
          json.put("ok", false);
          json.put("error", colaboradorVista.getMensajeError());
        } else {
          json.put("ok", true);
          json.put("colaborador", colaboradorVista);
        }
        break;
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
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    int colaboradorId =  Integer.parseInt(req.getParameter("colaboradorId"));
    int solicitudId =  Integer.parseInt(req.getParameter("solicitudId"));

    asignacionFacade.eliminarAsignacion(session.getAttribute("usuario"), colaboradorId, solicitudId);
  }
}
