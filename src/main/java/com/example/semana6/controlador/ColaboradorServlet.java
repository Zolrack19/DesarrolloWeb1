package com.example.semana6.controlador;

import java.io.IOException;
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
import jakarta.servlet.http.HttpSession;

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
      case "2" -> {
        colaboradoresDeSolicitud(req, resp);
      }
      default -> {}
    }
  }

  private void colaboradoresDeSolicitud(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    Object usuario = session.getAttribute("usuario");

    int solicitudId = Integer.parseInt(req.getParameter("solicitudId"));
    List<ColaboradorVista> colaboradorVistas = colaboradorFacade.getColaboradores(solicitudId,  usuario);
    
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(colaboradorVistas);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
  }

  private void buscarColaboradoresPorTokens(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String[] tokens = req.getParameterValues("token");
    List<ColaboradorVista> colaboradorVistas = colaboradorFacade.getColaboradores(tokens);

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(colaboradorVistas);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
  }

  private void cargarColaboradores(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    int numPag = Integer.parseInt(req.getParameter("numPag"));
    List<ColaboradorVista> colaboradoresVista = null;
    if (numPag > 0) {
      colaboradoresVista = colaboradorFacade.getColaboradores(numPag);
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


  @SuppressWarnings("unchecked")
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    
    HttpSession session = req.getSession(false);
    try {
      ObjectMapper mapper = new ObjectMapper();
      HashMap<String, Object> campos = mapper.readValue(req.getInputStream(), HashMap.class);
      ColaboradorVista colaboradorVista = colaboradorFacade.actualizarColaborador(campos, session.getAttribute("usuario"));

      Map<String, Object> json = new HashMap<>();
      json.put("ok", colaboradorVista != null);
      json.put("colaborador", colaboradorVista);
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
    int colaboradorId =  Integer.parseInt(req.getParameter("id"));
    colaboradorFacade.eliminarColaborador(session.getAttribute("usuario"), colaboradorId);
    
    // Map<String, Object> json = new HashMap<>();
    // json.put("ok", exito);
    // json.put("redirect", req.getContextPath() + "/index.html");
    // resp.setContentType("application/json");
    // resp.setCharacterEncoding("UTF-8");
    // new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
