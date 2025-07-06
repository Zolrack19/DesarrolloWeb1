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
  private StringBuilder mensajeError = new StringBuilder();


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
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();

    try {
      for (int i = 0; i < 1; i++) {
        
        String numeroDocumento = req.getParameter("documento");
        String docId = req.getParameter("tipoDocumentoId");
        String rolColabId = req.getParameter("rolColaboradorId");
        String nombre = req.getParameter("nombre");
        String apellidoP = req.getParameter("apellidoP");
        String apellidoM = req.getParameter("apellidoM");

        if (docId == null || !docId.matches("\\d+")) { // solo números
          mensajeError.append("Tipo de documento no válido: ").append(docId);
          break;
        }
        if (rolColabId == null || !rolColabId.matches("\\d+")) {
          mensajeError.append("Rol de colaborador inválido: ").append(rolColabId);
          break;
        }
        if (nombre == null) {
          mensajeError.append("\nNombre no puede ser nulo");
        } else {
          nombre = nombre.trim();
          if (nombre.isEmpty() || nombre.length() > 100) {
            mensajeError.append("\nNombre inválido (máximo 100 caracteres)");
          }
        }
        if (apellidoP == null || apellidoM == null) {
          mensajeError.append("\nApellidos no pueden estar en nulo");
        } else {
          apellidoP = apellidoP.trim();
          apellidoM = apellidoM.trim();
          if ((apellidoP.isEmpty() || apellidoM.isEmpty()) || (apellidoP.length() > 50 || apellidoM.length() > 50)) {
            mensajeError.append("\nApellidos inválidos (máximo 50 caracteres por apellido)");
          }
        }
        if (numeroDocumento == null || numeroDocumento.isEmpty()) { // esto mejorar
          mensajeError.append("\nNúmero de documento inválido: ").append(numeroDocumento);
        }

        if (!mensajeError.isEmpty()) break;
        
        short tipoDocumentoId = Short.valueOf(docId);
        short rolColaboradorId = Short.valueOf(rolColabId);

        ColaboradorCrear colaboradorCrear = new ColaboradorCrear(rolColaboradorId, tipoDocumentoId, numeroDocumento, nombre, apellidoP, apellidoM);
        ColaboradorVista colaboradorVista = colaboradorFacade.crearColaborador(colaboradorCrear);
        if (colaboradorVista.getMensajeError() != null) {
          json.put("ok", false);
          json.put("error", colaboradorVista.getMensajeError());
        } else {
          json.put("ok", true);
          json.put("colaborador", colaboradorVista);
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


  @SuppressWarnings("unchecked")
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();
    HttpSession session = req.getSession(false);

    try {
      for (int i = 0; i < 1; i++) {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> campos = mapper.readValue(req.getInputStream(), HashMap.class);
        
        int id = -1;
        short docId = -1;
        short rolColabId = -1;
        String numeroDocumento = null;
        String nombre = null;
        String apellidoP = null;
        String apellidoM = null;

        if (campos.get("id") instanceof Integer) {
          id = (int) campos.get("id");
        } else {
          mensajeError.append("\nError, no se proporcionó una id del colaborador");
          break;          
        }
        if (campos.get("documento") instanceof String) {
          numeroDocumento = ((String) campos.get("documento")).trim();
        } else {
          mensajeError.append("\nError en el tipo de dato del número de documento");
          break;          
        }
        if (campos.get("tipoDocumentoId") instanceof Short) {
          docId = (short) campos.get("tipoDocumentoId");
        } else {
          mensajeError.append("\nError en el tipo de documento");
          break; 
        }
        if (campos.get("rolColaboradorId") instanceof Short) {
          rolColabId = (short) campos.get("rolColaboradorId");
        } else {
          mensajeError.append("\nError en el tipo de documento");
          break; 
        }
        if (campos.get("nombre") instanceof String) {
          nombre = ((String) campos.get("nombre")).trim();
        } else {
          mensajeError.append("\nError en el dato del nombre");
          break;
        }
        if (campos.get("apellidoP") instanceof String) {
          apellidoP = ((String) campos.get("apellidoP")).trim();
        } else {
          mensajeError.append("\nError de dato en el apellido parteno");
          break;
        }
        if (campos.get("apellidoM") instanceof String) {
          apellidoM = ((String) campos.get("apellidoP")).trim();
        } else {
          mensajeError.append("\nError de dato en el apellido marteno");
          break;
        }

        ColaboradorCrear colaboradorCrear = new ColaboradorCrear(rolColabId, docId, numeroDocumento, nombre, apellidoP, apellidoM);
        colaboradorCrear.setId(id);
        ColaboradorVista colaboradorVista = colaboradorFacade.actualizarColaborador(colaboradorCrear, session.getAttribute("usuario"));
  
        json.put("ok", colaboradorVista != null);
        json.put("colaborador", colaboradorVista);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    new ObjectMapper().writeValue(resp.getWriter(), json);
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
