package com.example.semana6.controlador;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semana6.dto.colaborador.ColaboradorCrear;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.facade.ColaboradorFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ColaboradorServlet", urlPatterns = { "/control/ColaboradorServlet" })
public class ColaboradorServlet extends HttpServlet {
  private ColaboradorFacade colaboradorFacade = new ColaboradorFacade();


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    switch (action) {
      case null -> {cargarColaboradores(req, resp);}
      case "1" -> {
        buscarColaboradoresPorTokens(req, resp);
      }
      default -> {}
    }
  }

  private void buscarColaboradoresPorTokens(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String[] ids = req.getParameterValues("token");
    System.out.println(Arrays.toString(ids));
  }

  private void cargarColaboradores(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    int numPag = Integer.parseInt(req.getParameter("numPag"));
    List<ColaboradorVista> colaboradoresVista = null;
    if (numPag > 0) {
      colaboradoresVista = colaboradorFacade.getcolaboradores(numPag);
    }
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(colaboradoresVista);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    short tipoDocumentoId = Short.valueOf(req.getParameter("tipoDocumentoId"));
    String numeroDocumento = req.getParameter("documento");
    short rolColaboradorId = Short.valueOf(req.getParameter("rolColaboradorId"));
    String nombre = req.getParameter("nombre");
    String apellidoP = req.getParameter("apellidoP");
    String apellidoM = req.getParameter("apellidoM");

    ColaboradorCrear colaboradorCrear = new ColaboradorCrear(rolColaboradorId, tipoDocumentoId, numeroDocumento, nombre, apellidoP, apellidoM);
    ColaboradorVista colaboradorVista = colaboradorFacade.crearColaborador(colaboradorCrear);

    Map<String, Object> json = new HashMap<>();
    json.put("ok", colaboradorVista != null);
    json.put("colaborador", colaboradorVista);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
