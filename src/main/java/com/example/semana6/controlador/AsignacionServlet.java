package com.example.semana6.controlador;

import java.io.IOException;

import com.example.semana6.facade.AsignacionFacade;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AsignacionServlet", urlPatterns = { "/control/AsignacionServlet" })
public class AsignacionServlet extends HttpServlet {

  private AsignacionFacade asignacionFacade;

  @Override
  public void init() throws ServletException {
    asignacionFacade = new AsignacionFacade();
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    int colaboradorId =  Integer.parseInt(req.getParameter("colaboradorId"));
    int solicitudId =  Integer.parseInt(req.getParameter("solicitudId"));

    asignacionFacade.eliminarAsignacion(session.getAttribute("usuario"), colaboradorId, solicitudId);
  }
}
