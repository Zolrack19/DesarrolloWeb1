package com.example.semana6.facade;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.SectorEconomicoDAO;
import com.example.semana6.dao.TipoClienteDAO;
import com.example.semana6.dao.TipoDocumentoDAO;
import com.example.semana6.dto.cliente.ClienteCrear;
import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.cliente.PersonaConNegocioCrear;
import com.example.semana6.dto.cliente.PersonaConNegocioVista;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Cliente;
import com.example.semana6.modelo.PersonaConNegocio;
import com.example.semana6.modelo.SectorEconomico;
import com.example.semana6.modelo.TipoCliente;
import com.example.semana6.modelo.TipoDocumento;
import com.example.semana6.singleton.HibernateUtil;

public class ClienteFacade {
  private final ClienteDAO clienteDAO = new ClienteDAO();
  private final TipoClienteDAO tipoClienteDAO = new TipoClienteDAO();
  private final TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
  private final SectorEconomicoDAO sectorEconomicoDAO = new SectorEconomicoDAO();

  private StringBuilder query = new StringBuilder();
  
  public ClienteVista crearCliente(ClienteCrear clienteCrear) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {
      Cliente cliente = clienteCrear.toCliente();
      TipoCliente tipoCliente = tipoClienteDAO.getById(s, clienteCrear.getTipoClienteId());
      if (tipoCliente == null) {
        return new ClienteVista("Tipo de cliente inválido");
      }
      TipoDocumento tipoDocumento = tipoDocumentoDAO.getById(s, clienteCrear.getTipoDocumentoId());
      if (tipoDocumento == null) {
        return new ClienteVista("Tipo de documento inválido");
      }
      SectorEconomico sectorEconomico = sectorEconomicoDAO.getById(s, clienteCrear.getSectorEconomicoId());
      if (sectorEconomico == null) {
        return new ClienteVista("Entidad sector económico inválido");
      }
      
      cliente.setTipoCliente(tipoCliente);
      cliente.setTipoDocumento(tipoDocumento);
      cliente.setSectorEconomico(sectorEconomico);
      
      clienteDAO.crearCliente(s, cliente);
      
      ClienteVista clienteVista = new PersonaConNegocioVista(cliente.getRazonSocial(), cliente.getNumeroDocumento(),
      cliente.getTelefono(), cliente.getId(), cliente.getEmail(), cliente.getTipoDocumento().getNombre(), cliente.getTipoCliente().getNombre(),
      cliente.getSectorEconomico().getNombre());

      if (clienteCrear instanceof PersonaConNegocioCrear personaConNegocioCrear) {
        PersonaConNegocioVista clienteVista1 = (PersonaConNegocioVista) clienteVista;
        
        clienteVista1.setNombre(personaConNegocioCrear.getNombre());
        clienteVista1.setApellidoPaterno(personaConNegocioCrear.getApellidoPaterno());
        clienteVista1.setApellidoMaterno(personaConNegocioCrear.getApellidoMaterno());
      }
      s.getTransaction().commit();
      return clienteVista;
    } catch (Exception e) {
      s.getTransaction().rollback();
      e.printStackTrace();
      throw e;
    } finally {
      s.close();
    }
  }

  public List<ClienteVista> getClientes(String[] tokens) {
    if (tokens.length == 0) return null;
    query.append("""
    SELECT c.*, p.*,
    CASE WHEN p.id IS NOT NULL THEN 1 ELSE 0 END AS clazz_
    FROM cliente c LEFT JOIN persona_con_negocio p ON p.id = c.id WHERE 
    """);
    // System.out.println(query.length());

    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {
      
      int i = 0;
      while (i < 5 && tokens[i] != null) {
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
        i++;
      }
      query.append("limit 5");
      List<Cliente> clientes = clienteDAO.getClientesByQuery(s, query.toString());
      if (clientes.size() == 0) return null;
      List<ClienteVista> clientesVista = new LinkedList<>();
      for (Cliente cliente : clientes) {
        if (cliente instanceof PersonaConNegocio) {
          clientesVista.add(new PersonaConNegocioVista((PersonaConNegocio) cliente));
        } else {
          clientesVista.add(new ClienteVista(cliente));
        }
      }
      return clientesVista;
    } catch (Exception e) {
      throw e;
    } finally {
      s.close();
      query.setLength(0);
    }
  }

  public List<ClienteVista> getClientes(int numPag) {
    List<Cliente> clientes = clienteDAO.getRango((numPag - 1)*10, 10);
    // if (clientes.size() == 0) return null;
    List<ClienteVista> clientesVista = new LinkedList<>();
    clientes.forEach((cliente) -> {
      if (cliente instanceof PersonaConNegocio) {
        clientesVista.add(new PersonaConNegocioVista((PersonaConNegocio) cliente));
      } else {
        clientesVista.add(new ClienteVista(cliente));
      }
    });
    return clientesVista;
  }

  public ClienteVista actualizarCliente(Map<String, Object> campos, Object usuario) {
    if (!(usuario instanceof ColaboradorDTO) || !((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")
    || campos.get("id") == null) {
      return null;
    }
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    
    Cliente cliente = clienteDAO.getById(s, (int) campos.get("id"));
    if (cliente == null) {
      s.close();
      return null;
    }
    cliente.setNumeroDocumento((String) campos.get("documento"));
    cliente.setRazonSocial((String) campos.get("razon"));
    cliente.setContrasena((String) campos.get("contrasena"));
    cliente.setTelefono((String) campos.get("telefono"));
    if (cliente instanceof PersonaConNegocio personaConNegocio) {
      personaConNegocio.setNombre((String) campos.get("nombre"));
      personaConNegocio.setApellidoPaterno((String) campos.get("apellidoP"));
      personaConNegocio.setApellidoMaterno((String) campos.get("apellidoM"));
    }
    if (cliente.getTipoDocumento().getId() != Short.parseShort((String) campos.get("tipoDocumentoId"))) {
      TipoDocumento tipoDocumento = tipoDocumentoDAO.getById(s, Short.parseShort((String) campos.get("tipoDocumentoId")));
      if (tipoDocumento != null) {
        cliente.setTipoDocumento(tipoDocumento);
      }
    }
    if (cliente.getTipoCliente().getId() != Short.parseShort((String) campos.get("tipoClienteId"))) {
      TipoCliente tipoCliente = tipoClienteDAO.getById(s, Short.parseShort((String) campos.get("tipoClienteId")));
      if (tipoCliente != null) {
        cliente.setTipoCliente(tipoCliente);
      }
    }
    if (cliente.getTipoCliente().getId() != Short.parseShort((String) campos.get("tipoSectorEconomicoId"))) {
      SectorEconomico sectorEconomico = sectorEconomicoDAO.getById(s, Short.parseShort((String) campos.get("tipoSectorEconomicoId")));
      if (sectorEconomico != null) {
        cliente.setSectorEconomico(sectorEconomico);
      }
    }
    
    clienteDAO.actualizarCliente(s, cliente);
    
    s.getTransaction().commit();
    s.close();

    return new ClienteVista(cliente);
  }


  public void eliminarCliente(Object usuario, int clienteId) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    Cliente cliente = clienteDAO.getById(s, clienteId);
    if (cliente == null) {
      System.out.println("no hay cliente a eliminar");
      s.close();
      return;
    }
    if (usuario instanceof ClienteDTO) {
      s.close();
      return;
    } else if (usuario instanceof ColaboradorDTO) {
      if (!((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")) {
        s.close();
        return;
      }
    }
    clienteDAO.eliminarCliente(s, cliente);
    
    s.getTransaction().commit();
    s.close();
  }

}
