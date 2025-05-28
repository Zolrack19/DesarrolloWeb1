package com.example.semana6.facade;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.PersonaConNegocioDAO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioVista;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Cliente;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.PersonaConNegocio;

public class AutenticacionFacade {
  private final ClienteDAO clienteDAO;
  private final PersonaConNegocioDAO personaConNegocioDAO;
  private final ColaboradorDAO colaboradorDAO;

  public AutenticacionFacade(ClienteDAO clienteDAO, PersonaConNegocioDAO personaConNegocioDAO, ColaboradorDAO colaboradorDAO) {
    this.clienteDAO = clienteDAO;
    this.personaConNegocioDAO = personaConNegocioDAO;
    this.colaboradorDAO = colaboradorDAO;
  }
  
  public AutenticacionFacade() {
    this.clienteDAO = new ClienteDAO();
    this.personaConNegocioDAO = new PersonaConNegocioDAO();
    this.colaboradorDAO = new ColaboradorDAO();
  }


  public Object iniciarSesion(String email, String contrasena) {
    try {
      Colaborador colaborador = colaboradorDAO.getByEmail(email);
      if (colaborador != null) {
        if (!colaborador.getContrasena().equals(contrasena)) return null;
        ColaboradorVista colaboradorVista = new ColaboradorVista(colaborador);
        return colaboradorVista;
      }
  
      Cliente cliente = clienteDAO.getByEmail(email);
      if (cliente != null) {
        if (!cliente.getContrasena().equals(contrasena)) return null;
        if (cliente.getTipoCliente().getId() == 1) { //empresa
          ClienteVista clienteVista = new ClienteVista(cliente.getRazonSocial(), cliente.getNumeroDocumento(),
          cliente.getTelefono(), cliente.getId(), cliente.getEmail(), cliente.getTipoDocumento().getNombre(), cliente.getTipoCliente().getNombre(),
          cliente.getSectorEconomico().getNombre());
          return clienteVista;
        } else {
          PersonaConNegocio persona = personaConNegocioDAO.getById(cliente.getId());
          PersonaConNegocioVista clienteVista = new PersonaConNegocioVista(cliente.getRazonSocial(), cliente.getNumeroDocumento(),
          cliente.getTelefono(), cliente.getId(), cliente.getEmail(), cliente.getTipoDocumento().getNombre(), cliente.getTipoCliente().getNombre(),
          cliente.getSectorEconomico().getNombre(), persona.getNombre(), persona.getApellidoPaterno(), persona.getApellidoMaterno());
          return clienteVista;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
