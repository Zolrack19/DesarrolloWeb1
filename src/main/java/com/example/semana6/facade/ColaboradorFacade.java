package com.example.semana6.facade;

import java.util.LinkedList;
import java.util.List;
import org.hibernate.Session;

import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.RolColaboradorDAO;
import com.example.semana6.dao.SolicitudDAO;
import com.example.semana6.dao.TipoDocumentoDAO;
import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.colaborador.ColaboradorCrear;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.RolColaborador;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.modelo.TipoDocumento;
import com.example.semana6.singleton.HibernateUtil;

public class ColaboradorFacade {
  private ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
  private TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
  private RolColaboradorDAO rolColaboradorDAO = new RolColaboradorDAO();
  private AsignacionDAO asignacionDAO = new AsignacionDAO();
  private final SolicitudDAO solicitudDAO = new SolicitudDAO();
  private StringBuilder query = new StringBuilder();
  
  public ColaboradorVista crearColaborador(ColaboradorCrear colaboradorCrear) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {

      Colaborador colaborador = colaboradorCrear.toColaborador();
      TipoDocumento tipoDocumento = tipoDocumentoDAO.getById(s, colaboradorCrear.getTipoDocumentoId());
      if (tipoDocumento == null) {
        return new ColaboradorVista("Error, tipo de documento no es válido");
      }
      RolColaborador rolColaborador = rolColaboradorDAO.getById(colaboradorCrear.getRolColaboradorId());
      if (rolColaborador == null) {
        return new ColaboradorVista("Error, el tipo rol de colaborador no es válido");
      }

      colaborador.setTipoDocumento(tipoDocumento);
      colaborador.setRolColaborador(rolColaborador);
      colaboradorDAO.crearColaborador(colaborador);

      ColaboradorVista colaboradorVista = new ColaboradorVista(colaborador);
      s.getTransaction().commit();
      return colaboradorVista;
    } catch (Exception e) {
      s.getTransaction().rollback();
      throw e;
    } finally {
      s.close();
    }
  }

  public List<ColaboradorVista> getColaboradores(String[] tokens) {
    if (tokens.length == 0) return null;
    query.append("""
    SELECT c.*
    FROM colaboradordto_vista c WHERE c.solicitudes_activas < 5 and (
    """);

    for (int i = 0; i < Math.min(tokens.length, 5); i++) {
      String token = tokens[i];
      if (i != 0) {
        query.append(" OR ");
      }
      query.append("(\n");
      if (token.matches("\\d+")) {
        query.append("c.numero_documento LIKE ('").append(token).append("%')");
      } else {
        query.append("c.nombre ILIKE unaccent('%").append(token).append("%') OR \n"); 
        query.append("c.apellido_paterno ILIKE unaccent('%").append(token).append("%') OR \n"); 
        query.append("c.apellido_materno ILIKE unaccent('%").append(token).append("%')"); 
      }
      query.append("\n)\n");
    }
    query.append(") limit 5");
    List<ColaboradorVista> colaboradores = colaboradorDAO.getClientesByQuery(query.toString());
    query.delete(0, query.length());
    return colaboradores;
  }

  public List<ColaboradorVista> getColaboradores(int numPag) {
    List<ColaboradorVista> colaboradores = colaboradorDAO.getRangoVista((numPag - 1)*10, 10);
    if (colaboradores.size() == 0) return null;
    return colaboradores;
  }

  public List<ColaboradorVista> getColaboradores(int solicitudId, Object usuario) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    
    try {
      if (usuario instanceof ClienteVista clienteVista) { // solicitud no pertenece al cliente
        Solicitud solicitud = solicitudDAO.getById(s, solicitudId);
        if (solicitud == null || solicitud.getId() != clienteVista.getId()) return null;

      } else if (usuario instanceof ColaboradorVista colaboradorVista) {// Colaborador no pertenece en la solicitud
        if (!((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")) {
          if (asignacionDAO.getById(s, new AsignacionId(solicitudId, colaboradorVista.getId())) == null) return null;
        }
      }
      List<Asignacion> asignaciones = asignacionDAO.getByIdSolicitud(s, solicitudId);
      if (asignaciones == null) return null;
      
      List<ColaboradorVista> colaboradorVistas = new LinkedList<>();
      if (asignaciones.size() == 0) return colaboradorVistas; // se devuelve un array sin elementos
      // si hay coordinador, será el primera de la lista
      int coordinadorId = -1;
      if (asignaciones.get(0).getSolicitud().getCoordinador() != null) {
        coordinadorId = asignaciones.get(0).getSolicitud().getCoordinador().getId();
      }

      for (Asignacion asignacion : asignaciones) {
        Colaborador colaborador = asignacion.getColaborador();
        if (coordinadorId != -1 && coordinadorId == colaborador.getId()) {
          colaboradorVistas.addFirst(new ColaboradorVista(colaborador));
          coordinadorId = 0;
          continue;
        }
        colaboradorVistas.add(new ColaboradorVista(colaborador));
      }
      return colaboradorVistas;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      s.close();
    }
  }

  public ColaboradorVista actualizarColaborador(ColaboradorCrear colaboradorCrear, Object usuario) {
    if (!(usuario instanceof ColaboradorDTO) || !((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")) {
      return new ColaboradorVista("Error, no cuenta con permisos para actualizar atributos de los colaboradores.");
    }

    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    
    try {
      Colaborador colaborador = colaboradorDAO.getById(s, colaboradorCrear.getId());
      if (colaborador == null) {
        return new ColaboradorVista("Error, el colaborador que intenta actualizar no existe");
      }
      colaborador.setNombre(colaboradorCrear.getNombre());
      colaborador.setApellidoPaterno(colaboradorCrear.getApellidoPaterno());
      colaborador.setApellidoMaterno(colaboradorCrear.getApellidoMaterno());
      colaborador.setNumeroDocumento(colaboradorCrear.getNumeroDocumento());
      if (colaborador.getTipoDocumento().getId() != colaboradorCrear.getTipoDocumentoId()) {
        TipoDocumento tipoDocumento = tipoDocumentoDAO.getById(s, colaboradorCrear.getTipoDocumentoId());
        if (tipoDocumento == null) {
          return new ColaboradorVista("Error, tipo de documento no válido");
        }
        colaborador.setTipoDocumento(tipoDocumento);
      }
      if (colaborador.getRolColaborador().getId() != colaboradorCrear.getRolColaboradorId()) {
        RolColaborador rolColaborador = rolColaboradorDAO.getById(colaboradorCrear.getRolColaboradorId());
        if (rolColaborador == null) {
          return new ColaboradorVista("Error, rol de colaborador no válido");
        }
        colaborador.setRolColaborador(rolColaborador);
      }
  
      colaboradorDAO.actualizarColaborador(s, colaborador);
      ColaboradorVista colaboradorVista = new ColaboradorVista(colaborador);

      s.getTransaction().commit();
      return colaboradorVista;
    } catch (Exception e) {
      s.getTransaction().rollback();
      throw e;
    } finally {
      s.close();
    }
  }

  public void eliminarColaborador(Object usuario, int colaboradorId) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    Colaborador colaborador = colaboradorDAO.getById(s, colaboradorId);
    if (colaborador == null) {
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
    colaboradorDAO.eliminarColaborador(s, colaborador);
    
    s.getTransaction().commit();
    s.close();
  }
}
