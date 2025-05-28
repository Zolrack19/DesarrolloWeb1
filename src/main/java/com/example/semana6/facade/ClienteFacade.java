package com.example.semana6.facade;

import java.util.ArrayList;
import java.util.List;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.SectorEconomicoDAO;
import com.example.semana6.dao.TipoClienteDAO;
import com.example.semana6.dao.TipoDocumentoDAO;
import com.example.semana6.dto.cliente.ClienteCrear;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioCrear;
import com.example.semana6.dto.cliente.PersonaConNegocioVista;
import com.example.semana6.modelo.Cliente;

public class ClienteFacade {
  private final ClienteDAO clienteDAO = new ClienteDAO();
  // private final PersonaConNegocioDAO personaConNegocioDAO = new PersonaConNegocioDAO();
  private final TipoClienteDAO tipoClienteDAO = new TipoClienteDAO();
  private final TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
  private final SectorEconomicoDAO sectorEconomicoDAO = new SectorEconomicoDAO();
  
  public ClienteVista crearCliente(ClienteCrear clienteCrear) {
    try {
      Cliente cliente = clienteCrear.toCliente();
      cliente.setTipoCliente(tipoClienteDAO.getById(clienteCrear.getTipoClienteId()));
      cliente.setTipoDocumento(tipoDocumentoDAO.getById(clienteCrear.getTipoDocumentoId()));
      cliente.setSectorEconomico(sectorEconomicoDAO.getById(clienteCrear.getSectorEconomicoId()));
      
      clienteDAO.crearCliente(cliente);
      
      ClienteVista clienteVista = new PersonaConNegocioVista(cliente.getRazonSocial(), cliente.getNumeroDocumento(),
      cliente.getTelefono(), cliente.getId(), cliente.getEmail(), cliente.getTipoDocumento().getNombre(), cliente.getTipoCliente().getNombre(),
      cliente.getSectorEconomico().getNombre());

      if (clienteCrear instanceof PersonaConNegocioCrear) {
        clienteVista = (PersonaConNegocioVista) clienteVista;
        ((PersonaConNegocioVista) clienteVista).setNombre(((PersonaConNegocioCrear) clienteCrear).getNombre());
        ((PersonaConNegocioVista) clienteVista).setApellidoPaterno(((PersonaConNegocioCrear) clienteCrear).getApellidoPaterno());
        ((PersonaConNegocioVista) clienteVista).setApellidoMaterno(((PersonaConNegocioCrear) clienteCrear).getApellidoMaterno());
      }
      System.out.println("todo biennnnnn!!");
      return clienteVista;  
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<ClienteVista> getClientes(int idLimite, int maxResultados, boolean paginaSiguiente) {
    List<Cliente> clientes = clienteDAO.getPaginaDescendiente(idLimite, maxResultados, paginaSiguiente);
    if (clientes.size() == 0) return null;
    List<ClienteVista> clientesVista = new ArrayList<>();
    clientes.forEach((cliente) -> {
      clientesVista.add(new ClienteVista(cliente));
    });
    return clientesVista;
  }

}
