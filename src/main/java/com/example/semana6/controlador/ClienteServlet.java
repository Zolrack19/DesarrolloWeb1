package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@WebServlet(name = "ClienteServlet", urlPatterns = {"/control/ClienteServlet"})
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
      case null -> {cargarClientes(req, resp);}
      case "1" -> {
        buscarClientesPorTokens(req, resp);
      }
      default -> {}
    }
  }

  private void buscarClientesPorTokens(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    short tipoDocumentoId = Short.valueOf(req.getParameter("tipoDocumentoId"));
    String numeroDocumento = req.getParameter("documento");
    short tipoClienteId = Short.valueOf(req.getParameter("tipoClienteId"));
    short tipoSectorEconomicoId = Short.valueOf(req.getParameter("tipoSectorEconomicoId"));
    String razon = req.getParameter("razon");
    String email = req.getParameter("email");
    String contrasena = req.getParameter("contrasena");
    String telefono = req.getParameter("telefono");

    ClienteCrear clienteCrear;
    if (tipoClienteId == 1) { //empresa
      clienteCrear = new ClienteCrear(tipoDocumentoId, tipoClienteId, tipoSectorEconomicoId, razon, numeroDocumento, email, contrasena, telefono);
    } else {
      String nombre = req.getParameter("nombre");
      String apellidoP = req.getParameter("apellidoP");
      String apellidoM = req.getParameter("apellidoM");
      clienteCrear = new PersonaConNegocioCrear(tipoDocumentoId, tipoClienteId, tipoSectorEconomicoId, razon, numeroDocumento, email, contrasena, telefono,
      nombre, apellidoP, apellidoM);
    }

    ClienteVista clienteVista = clienteFacade.crearCliente(clienteCrear);
    
    Map<String, Object> json = new HashMap<>();
    json.put("ok", clienteVista != null);
    json.put("cliente", clienteVista);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
