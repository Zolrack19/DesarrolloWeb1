package com.example.semana6.facade;

import java.util.Map;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.PersonaConNegocioDAO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioVista;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Cliente;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.PersonaConNegocio;

public class PerfilFacade {
  private ClienteDAO clienteDAO = new ClienteDAO();
  private PersonaConNegocioDAO personaConNegocioDAO = new PersonaConNegocioDAO();
  private ColaboradorDAO colaboradorDAO = new ColaboradorDAO();

  public boolean actualizarAtributos(Map<String, Object> campos, Object usuario) {
    try {
      
      String query = "";
  
      if (campos.containsKey("razon")) {
        if (usuario instanceof ClienteVista) {
          query = "UPDATE Cliente c SET c.razonSocial = ?1 WHERE c.id = ?2";
          String razon = (String) campos.get("razon");
          clienteDAO.actualizarUnAtributo(query, ((ClienteVista) usuario).getId(), razon);
          ((ClienteVista) usuario).setRazonSocial(razon);
        }
      }
      
      if (campos.containsKey("telefono")) {
        if (usuario instanceof ClienteVista) {
          query = "UPDATE Cliente c SET c.telefono = ?1 WHERE c.id = ?2";
          String telefono = (String) campos.get("telefono");
          clienteDAO.actualizarUnAtributo(query, ((ClienteVista) usuario).getId(), telefono);
          ((ClienteVista) usuario).setTelefono(telefono);
        }
      }
  
      if (campos.containsKey("contrasena")) {
        if (!campos.get("nuevaContrasena").equals(campos.get("confirmContrasena"))) return false;
        if (usuario instanceof ClienteVista) {
          Cliente cliente = clienteDAO.getById(((ClienteVista) usuario).getId());
          if (cliente == null) return false;
          if (cliente.getContrasena().equals(campos.get("contrasena"))) {
            query = "UPDATE Cliente c SET c.contrasena = ?1 WHERE c.id = ?2";
            clienteDAO.actualizarUnAtributo(query, cliente.getId(), campos.get("nuevaContrasena"));
          }
  
        } else if (usuario instanceof ColaboradorVista) {
          Colaborador colaborador = colaboradorDAO.getById(((ColaboradorVista) usuario).getId());
          if (colaborador == null) return false;
          if (colaborador.getContrasena().equals(campos.get("contrasena"))) {
            query = "UPDATE Cliente c SET c.contrasena = ?1 WHERE c.id = ?2";
            colaborador.setContrasena((String) campos.get("contrasena"));
            colaboradorDAO.actualizarColaborador(colaborador);
          }
        }
      }
      
      if (campos.containsKey("nombre")) {

        String nombre = (String) campos.get("nombre");
        String apellidoPaterno = (String) campos.get("apellidoPaterno");
        String apellidoMaterno = (String) campos.get("apellidoMaterno");

        if (usuario instanceof PersonaConNegocioVista) {
          PersonaConNegocio cliente = personaConNegocioDAO.getById(((PersonaConNegocioVista) usuario).getId());
          if (cliente == null) return false;
          cliente.setNombre(nombre);
          cliente.setApellidoPaterno(apellidoPaterno);
          cliente.setApellidoMaterno(apellidoMaterno);
          personaConNegocioDAO.actualizarPersonaConNegocio(cliente);
          ((PersonaConNegocioVista) usuario).setNombre(nombre);
          ((PersonaConNegocioVista) usuario).setApellidoPaterno(apellidoPaterno);
          ((PersonaConNegocioVista) usuario).setApellidoMaterno(apellidoMaterno);
  
        } else if (usuario instanceof ColaboradorVista) {
          Colaborador colaborador = colaboradorDAO.getById(((ColaboradorVista) usuario).getId());
          if (colaborador == null) return false;
          colaborador.setNombre(nombre);
          colaborador.setApellidoPaterno(apellidoPaterno);
          colaborador.setApellidoMaterno(apellidoMaterno);
          colaboradorDAO.actualizarColaborador(colaborador);
          ((ColaboradorVista) usuario).setNombre(nombre);
          ((ColaboradorVista) usuario).setApellidoPaterno(apellidoPaterno);
          ((ColaboradorVista) usuario).setApellidoMaterno(apellidoMaterno);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean eliminarPerfil(Object usuario) {
    if (usuario instanceof ClienteVista) {
      clienteDAO.eliminarClienteById(((ClienteVista) usuario).getId());
    } else if (usuario instanceof ColaboradorVista) {
      colaboradorDAO.eliminarColaboradorById(((ColaboradorVista) usuario).getId());
    }
    usuario = null;
    return true;
  }
}
