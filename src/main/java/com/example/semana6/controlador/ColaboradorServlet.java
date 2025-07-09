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
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    Map<String, Object> json = new HashMap<>();
    json.put("ok", false);
    
    String action = req.getParameter("action");
    try {
      switch (action) {
        case null -> {cargarColaboradores(req, resp, json);}
        case "1" -> {
          buscarColaboradoresPorTokens(req, resp, json);
        }
        case "2" -> {
          colaboradoresDeSolicitud(req, resp);
        }
        default -> {}
      }
    } catch (Exception e) {
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }

    new ObjectMapper().writeValue(resp.getWriter(), json);
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

  private void buscarColaboradoresPorTokens(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> json) throws ServletException, IOException {
    do {
      String txtTokens = req.getParameter("txtTokens");
      String estri = req.getParameter("estricto"); 
      
      if (estri == null) {
        json.put("error", "Parámetro 'solicitudesActivas' no especificado"); break;
      }
      if (!"true".equalsIgnoreCase(estri) && !"false".equalsIgnoreCase(estri)) {
        json.put("error", "Parámetro 'solicitudesActivas' no puede ser otro mas que booleano: " + estri); break;
      }
      if (txtTokens == null || txtTokens.length() == 0) {
        json.put("error", "Texto de búsqueda inválido: " + txtTokens); break;
      }

      boolean estricto = Boolean.parseBoolean(estri);
      String dummy[] = txtTokens.split(" ");
      String[] tokens = new String[5];
      int j = 0;
      for (int i = 0; i < dummy.length; i++) {
        if (dummy[i].length() > 3) {
          tokens[j] = dummy[i];
          j++;
          if (j >= 5) break;
        }
      }
      
      List<ColaboradorVista> colaboradorVista = colaboradorFacade.getColaboradores(tokens, estricto);
      json.put("ok", true);
      json.put("colaboradores", colaboradorVista);
    } while (false);
  }

  private void cargarColaboradores(HttpServletRequest req, HttpServletResponse resp,  Map<String, Object> json) throws ServletException, IOException {
    int numPag = Integer.parseInt(req.getParameter("numPag"));
    List<ColaboradorVista> colaboradoresVista = null;
    if (numPag > 0) {
      colaboradoresVista = colaboradorFacade.getColaboradores(numPag);
    }

    json.put("ok", colaboradoresVista != null);
    json.put("colaboradores", colaboradoresVista);

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
        String numeroDocumento = req.getParameter("documento");
        String docId = req.getParameter("tipoDocumentoId");
        String rolColabId = req.getParameter("rolColaboradorId");
        String nombre = req.getParameter("nombre");
        String apellidoP = req.getParameter("apellidoP");
        String apellidoM = req.getParameter("apellidoM");

        if (docId == null || !docId.matches("\\d+")) { // solo números
          json.put("error", "Tipo de documento no válido: " + docId); break;
        }
        if (rolColabId == null || !rolColabId.matches("\\d+")) {
          json.put("error", "Rol de colaborador inválido: " + rolColabId); break;
        }
        if (nombre == null) {
          json.put("error", "Nombre no puede ser nulo"); break;
        } else {
          nombre = nombre.trim();
          if (nombre.isEmpty() || nombre.length() > 100) {
            json.put("error", "Nombre inválido (máximo 100 caracteres)"); break;
          }
        }
        if (apellidoP == null || apellidoM == null) {
          json.put("error", "Apellidos no pueden estar en nulo"); break;
        } else {
          apellidoP = apellidoP.trim();
          apellidoM = apellidoM.trim();
          if ((apellidoP.isEmpty() || apellidoM.isEmpty()) || (apellidoP.length() > 50 || apellidoM.length() > 50)) {
            json.put("error", "Apellidos inválidos (máximo 50 caracteres por apellido)"); break;
          }
        }
        if (numeroDocumento == null || numeroDocumento.isEmpty()) { // esto mejorar
          json.put("error", "Número de documento inválido: " + numeroDocumento); break;
        }

        short tipoDocumentoId = Short.valueOf(docId);
        short rolColaboradorId = Short.valueOf(rolColabId);

        ColaboradorCrear colaboradorCrear = new ColaboradorCrear(rolColaboradorId, tipoDocumentoId, numeroDocumento, nombre, apellidoP, apellidoM);
        ColaboradorVista colaboradorVista = colaboradorFacade.crearColaborador(colaboradorCrear);
        if (colaboradorVista.getMensajeError() != null) {
          json.put("error", colaboradorVista.getMensajeError());
        } else {
          json.put("ok", true);
          json.put("colaborador", colaboradorVista);
        }
      } while (false);
    } catch (Exception e) {
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }
    
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }


  @SuppressWarnings("unchecked")
  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    HttpSession session = req.getSession(false);
    Map<String, Object> json = new HashMap<>();
    json.put("ok", false);
    
    try {
      do {
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
          json.put("error", "Error, no se proporcionó una id del colaborador"); break;
        }
        if (campos.get("documento") instanceof String) {
          numeroDocumento = ((String) campos.get("documento")).trim();
        } else {
          json.put("error", "Error en el tipo de dato del número de documento"); break;
        }
        if (campos.get("tipoDocumentoId") instanceof Short) {
          docId = (short) campos.get("tipoDocumentoId");
        } else {
          json.put("error", "Error en el tipo de documento"); break;
        }
        if (campos.get("rolColaboradorId") instanceof Short) {
          rolColabId = (short) campos.get("rolColaboradorId");
        } else {
          json.put("error", "Error en el tipo de documento"); break;
        }
        if (campos.get("nombre") instanceof String) {
          nombre = ((String) campos.get("nombre")).trim();
        } else {
          json.put("error", "Error en el dato del nombre"); break;
        }
        if (campos.get("apellidoP") instanceof String) {
          apellidoP = ((String) campos.get("apellidoP")).trim();
        } else {
          json.put("error", "Error de dato en el apellido parteno"); break;
        }
        if (campos.get("apellidoM") instanceof String) {
          apellidoM = ((String) campos.get("apellidoP")).trim();
        } else {
          json.put("error", "Error de dato en el apellido marteno"); break;
        }

        ColaboradorCrear colaboradorCrear = new ColaboradorCrear(rolColabId, docId, numeroDocumento, nombre, apellidoP, apellidoM);
        colaboradorCrear.setId(id);
        ColaboradorVista colaboradorVista = colaboradorFacade.actualizarColaborador(colaboradorCrear, session.getAttribute("usuario"));
  
        json.put("ok", colaboradorVista != null);
        json.put("colaborador", colaboradorVista);
      } while (false);

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
