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
  private final TipoClienteDAO tipoClienteDAO = new TipoClienteDAO();
  private final TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
  private final SectorEconomicoDAO sectorEconomicoDAO = new SectorEconomicoDAO();

  private StringBuilder query = new StringBuilder("""
    SELECT c.*, p.*,
    CASE WHEN p.id IS NOT NULL THEN 1 ELSE 0 END AS clazz_
    FROM cliente c LEFT JOIN persona_con_negocio p ON p.id = c.id WHERE 
  """);
  
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

  public List<Cliente> getClientes(String[] tokens) {
    if (tokens.length == 0) return null;
    System.out.println(query.length());
    // query.delete(34, query.length());

    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      if (i != 0) {
        query.append(" OR ");
      }
      query.append("(\n");
      query.append("c.razon_social ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.numero_documento LIKE ('").append(token); 
      query.append("%')");
      query.append(")\n");
    }
    System.err.println('\n');
    System.err.println('\n');
    System.out.println(query.toString());
    System.err.println('\n');
    System.err.println('\n');
    List<Cliente> clientes = clienteDAO.getClientesByQuery(query.toString());
    return clientes;
  }

  public List<ClienteVista> getClientes(int numPag) {
    List<Cliente> clientes = clienteDAO.getRango((numPag - 1)*10, 10);
    if (clientes.size() == 0) return null;
    List<ClienteVista> clientesVista = new ArrayList<>();
    clientes.forEach((cliente) -> {
      clientesVista.add(new ClienteVista(cliente));
    });
    return clientesVista;
  }

}
