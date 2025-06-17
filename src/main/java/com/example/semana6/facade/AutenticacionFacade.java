package com.example.semana6.facade;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioVista;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Cliente;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.PersonaConNegocio;

public class AutenticacionFacade {
  private final ClienteDAO clienteDAO;
  private final ColaboradorDAO colaboradorDAO;

  public AutenticacionFacade() {
    this.clienteDAO = new ClienteDAO();
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
      if (cliente == null || !cliente.getContrasena().equals(contrasena)) return null;
      ClienteVista clienteVista = null;
      if (cliente instanceof PersonaConNegocio) {
        clienteVista = new PersonaConNegocioVista((PersonaConNegocio) cliente);
      } else {
        clienteVista = new ClienteVista(cliente);
      }
      return clienteVista;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
