package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semana6.ValidacionNegocioException;
import com.example.semana6.dto.cliente.ClienteCrear;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioCrear;
import com.example.semana6.facade.ClienteFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ClienteServlet", urlPatterns = { "/control/ClienteServlet" })
public class ClienteServlet extends HttpServlet {
  private ClienteFacade clienteFacade;

  @Override
  public void init() throws ServletException {
    clienteFacade = new ClienteFacade();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    switch (action) {
      case null -> {
        cargarClientes(req, resp);
      }
      case "1" -> {
        buscarClientesPorTokens(req, resp);
      }
      default -> {
      }
    }
  }

  private void buscarClientesPorTokens(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String[] tokens = req.getParameterValues("token");
    List<ClienteVista> clientesVista = clienteFacade.getClientes(tokens);

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(clientesVista);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    resp.getWriter().write(json);
  }

  private void cargarClientes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    int numPag = Integer.parseInt(req.getParameter("numPag"));
    List<ClienteVista> clientesVista = null;
    if (numPag > 0) {
      clientesVista = clienteFacade.getClientes(numPag);
    }
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(clientesVista);
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
    System.out.println("hola");

    try {
      // === Validaciones simples de parámetros ===
      String docId = req.getParameter("tipoDocumentoId");
      String tpClienteId = req.getParameter("tipoClienteId");
      String tpSector = req.getParameter("tipoSectorEconomicoId");
      String numeroDocumento = req.getParameter("documento");
      String razon = req.getParameter("razon");
      String email = req.getParameter("email");
      String contrasena = req.getParameter("contrasena");
      String telefono = req.getParameter("telefono");

      if (telefono == null || !telefono.matches("^9\\d{8}$")) {
        throw new IllegalArgumentException("Teléfono inválido (debe comenzar con 9 y tener 9 dígitos)");
      }
      if (razon == null || razon.length() > 200) {
        throw new IllegalArgumentException("Razón social inválida (máximo 200 caracteres)");
      }
      if (email == null || email.length() > 100) {
        throw new IllegalArgumentException("Email inválido (máximo 100 caracteres)");
      }
      if (contrasena == null || contrasena.length() < 8 || contrasena.length() > 50) {
        throw new IllegalArgumentException("Contraseña inválida (debe tener entre 8 y 50 caracteres)");
      }
      if (docId == null || !docId.matches("\\d+")) {
        throw new IllegalArgumentException("Tipo de documento no válido");
      }
      if (tpClienteId == null || !tpClienteId.matches("\\d+")) {
        throw new IllegalArgumentException("Tipo de cliente no válido");
      }
      if (tpSector == null || !tpSector.matches("\\d+")) {
        throw new IllegalArgumentException("Sector económico no válido");
      }

      // === Conversión segura ===
      short tipoDocumentoId = Short.parseShort(docId);
      short tipoClienteId = Short.parseShort(tpClienteId);
      short tipoSectorEconomicoId = Short.parseShort(tpSector);

      // === Creación del objeto ===
      ClienteCrear clienteCrear;
      if (tipoClienteId == 1) {
        clienteCrear = new ClienteCrear(tipoDocumentoId, tipoClienteId, tipoSectorEconomicoId, razon, numeroDocumento,
            email, contrasena, telefono);
      } else {
        String nombre = req.getParameter("nombre");
        String apellidoP = req.getParameter("apellidoP");
        String apellidoM = req.getParameter("apellidoM");
        clienteCrear = new PersonaConNegocioCrear(tipoDocumentoId, tipoClienteId, tipoSectorEconomicoId, razon,
          numeroDocumento, email, contrasena, telefono,
          nombre, apellidoP, apellidoM);
      }

      ClienteVista clienteVista = clienteFacade.crearCliente(clienteCrear);

      json.put("ok", clienteVista != null);
      json.put("cliente", clienteVista);

    } catch (IllegalArgumentException e) {
      json.put("ok", false);
      json.put("error", e.getMessage());
    } catch (ValidacionNegocioException e) {
      json.put("ok", false);
      json.put("error", e.getMessage());
    } catch (Exception e) {
      json.put("ok", false);
      json.put("error", "Error inesperado en el servidor");
      e.printStackTrace();
    }

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
      ClienteVista clienteVista = clienteFacade.actualizarCliente(campos, session.getAttribute("usuario"));

      Map<String, Object> json = new HashMap<>();
      json.put("ok", clienteVista != null);
      json.put("cliente", clienteVista);
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
    int clienteId = Integer.parseInt(req.getParameter("id"));
    clienteFacade.eliminarCliente(session.getAttribute("usuario"), clienteId);

    // Map<String, Object> json = new HashMap<>();
    // json.put("ok", exito);
    // json.put("redirect", req.getContextPath() + "/index.html");
    // resp.setContentType("application/json");
    // resp.setCharacterEncoding("UTF-8");
    // new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
