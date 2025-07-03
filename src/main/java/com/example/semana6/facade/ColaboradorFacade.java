package com.example.semana6.facade;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.RolColaboradorDAO;
import com.example.semana6.dao.TipoDocumentoDAO;
import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.colaborador.ColaboradorCrear;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.singleton.HibernateUtil;

public class ColaboradorFacade {
  private ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
  private TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
  private RolColaboradorDAO rolColaboradorDAO = new RolColaboradorDAO();
  private AsignacionDAO asignacionDAO = new AsignacionDAO();
  private StringBuilder query = new StringBuilder();
  
  public ColaboradorVista crearColaborador(ColaboradorCrear colaboradorCrear) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {

      Colaborador colaborador = colaboradorCrear.toColaborador();
      colaborador.setTipoDocumento(tipoDocumentoDAO.getById(s, colaboradorCrear.getTipoDocumentoId()));
      colaborador.setRolColaborador(rolColaboradorDAO.getById(colaboradorCrear.getRolColaboradorId()));
      colaboradorDAO.crearColaborador(colaborador);
      ColaboradorVista colaboradorVista = new ColaboradorVista(colaborador);
      s.getTransaction().commit();
      s.close();
      return colaboradorVista;
    } catch (Exception e) {
      s.getTransaction().rollback();
      e.printStackTrace();
    }
    s.close();
    return null;
  }

  public List<ColaboradorVista> getColaboradores(String[] tokens) {
    if (tokens.length == 0) return null;
    query.append("""
      SELECT c.*
      FROM colaboradordto_vista c WHERE 
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
      query.append(" and c.solicitudes_activas < 5 )\n");
    }
    query.append("limit 5");
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
      if (usuario instanceof ClienteDTO) {
        if (asignacionDAO.getById(s, new AsignacionId(solicitudId, ((ClienteVista) usuario).getId())) == null) return null;
      } else if (usuario instanceof ColaboradorDTO) {
        if (!((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")) {
          if (asignacionDAO.getById(s, new AsignacionId(solicitudId, ((ColaboradorVista) usuario).getId())) == null) return null;
        }
      }
      List<Asignacion> asignaciones = asignacionDAO.getByIdSolicitud(s, solicitudId);
      if (asignaciones != null && asignaciones.size() == 0) return null;
      
      List<ColaboradorVista> colaboradorVistas = new LinkedList<>();
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
        }
        colaboradorVistas.add(new ColaboradorVista(colaborador));
      }
      s.close();
      return colaboradorVistas;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      s.close();
    }

    return null;
  }

  public ColaboradorVista actualizarColaborador(Map<String, Object> campos, Object usuario) {
    if (!(usuario instanceof ColaboradorDTO) || !((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")
    || campos.get("id") == null) {
      return null;
    }
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    
    Colaborador colaborador = colaboradorDAO.getById(s, (int) campos.get("id"));
    if (colaborador == null) {
      s.close();
      return null;
    }
    colaborador.setNombre((String) campos.get("nombre"));
    colaborador.setApellidoPaterno((String) campos.get("apellidoP"));
    colaborador.setApellidoMaterno((String) campos.get("apellidoM"));
    colaborador.setNumeroDocumento((String) campos.get("documento"));
    if (colaborador.getTipoDocumento().getId() != Short.parseShort((String) campos.get("tipoDocumentoId"))) {
      colaborador.setTipoDocumento(tipoDocumentoDAO.getById(s, Short.parseShort((String) campos.get("tipoDocumentoId"))));
    }
    if (colaborador.getRolColaborador().getId() != Short.parseShort((String) campos.get("rolColaboradorId"))) {
      colaborador.setRolColaborador(rolColaboradorDAO.getById(Short.parseShort((String) campos.get("rolColaboradorId"))));
    }

    colaboradorDAO.actualizarColaborador(s, colaborador);
    
    s.getTransaction().commit();
    s.close();

    return new ColaboradorVista(colaborador);
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
