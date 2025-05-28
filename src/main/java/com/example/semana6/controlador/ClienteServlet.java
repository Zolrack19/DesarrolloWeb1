package com.example.semana6.controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semana6.dto.cliente.ClienteCrear;
import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioCrear;
import com.example.semana6.dto.cliente.PersonaConNegocioVista;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.facade.ClienteFacade;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ClienteServlet", urlPatterns = {"/control/ClienteServlet"})
public class ClienteServlet extends HttpServlet {
  private ClienteFacade clienteFacade;

  @Override
  public void init() throws ServletException {
    clienteFacade = new ClienteFacade();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    int idLimite = Integer.parseInt(req.getParameter("idLimite"));
    int maxResultados = Integer.parseInt(req.getParameter("maxResultados"));
    boolean paginaSiguiente = Boolean.parseBoolean(req.getParameter("paginaSiguiente"));

    List<ClienteVista> clientesVista = clienteFacade.getClientes(idLimite, maxResultados, paginaSiguiente);

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

    boolean ok = false;
    boolean esPersonaConNegocio = false;

    ClienteCrear clienteCrear;
    if (tipoClienteId == 1) { //empresa
      clienteCrear = new ClienteCrear(tipoDocumentoId, tipoClienteId, tipoSectorEconomicoId, razon, numeroDocumento, email, contrasena, telefono);
    } else {
      String nombre = req.getParameter("nombre");
      String apellidoP = req.getParameter("apellidoP");
      String apellidoM = req.getParameter("apellidoM");
      clienteCrear = new PersonaConNegocioCrear(tipoDocumentoId, tipoClienteId, tipoSectorEconomicoId, razon, numeroDocumento, email, contrasena, telefono,
      nombre, apellidoP, apellidoM);
      esPersonaConNegocio = true;
    }

    ClienteVista clienteVista = clienteFacade.crearCliente(clienteCrear);

    if (clienteVista != null) {
      HttpSession session = req.getSession();
      session.setAttribute("usuario", esPersonaConNegocio ? ((PersonaConNegocioVista) clienteVista) : clienteVista);
      session.setAttribute("rol", "cliente");
      ok = true;
    }

    
    Map<String, Object> json = new HashMap<>();
    json.put("ok", ok);
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(resp.getWriter(), json);
  }
}
