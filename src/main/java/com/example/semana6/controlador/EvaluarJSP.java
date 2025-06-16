package com.example.semana6.controlador;

import java.io.IOException;

import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/control/EvaluarJSP" })
public class EvaluarJSP extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
    String vista = req.getParameter("vista");
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");
    boolean bloquear = false;

    if (usuario instanceof ClienteDTO) {
      switch (vista) {
        case "clientes.jsp":
        case "colaboradores.jsp":
          bloquear = true;
          break;
        default:
      }
    } else if (usuario instanceof ColaboradorDTO) {
      ColaboradorVista colaboradorVista = (ColaboradorVista) usuario;
      if (!colaboradorVista.getRolColaborador().equals("Administrador")) {
        switch (vista) {
          case "clientes.jsp":
          case "colaboradores.jsp":
            bloquear = true;
            break;
          default:
        }
      }
    }

    if (bloquear) {
      resp.sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    RequestDispatcher rd = req.getRequestDispatcher("/html/" + vista);
    rd.include(req, resp);

  }
}
