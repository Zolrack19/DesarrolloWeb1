package com.example.semana6.facade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.RolColaboradorDAO;
import com.example.semana6.dao.TipoDocumentoDAO;
import com.example.semana6.dto.cliente.ClienteDTO;
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
  
  public ColaboradorVista crearColaborador(ColaboradorCrear colaboradorcCrear) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {

      Colaborador colaborador = colaboradorcCrear.toColaborador();
      colaborador.setTipoDocumento(tipoDocumentoDAO.getById(s, colaboradorcCrear.getTipoDocumentoId()));
      colaborador.setRolColaborador(rolColaboradorDAO.getById(colaboradorcCrear.getRolColaboradorId()));
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

    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      if (i != 0) {
        query.append(" OR ");
      }
      query.append("(\n");
      query.append("c.nombre ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.apellido_paterno ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.apellido_materno ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.numero_documento LIKE ('").append(token); 
      query.append("%')");
      query.append(")\n");
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
    try {
      if (usuario instanceof ClienteDTO) {
      } else if (usuario instanceof ColaboradorDTO) {
        if (((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")) {
          List<Asignacion> asignaciones = asignacionDAO.getByIdSolicitud(solicitudId);
          List<ColaboradorVista> colaboradorVistas = new LinkedList<>();
          for (Asignacion asignacion : asignaciones) {
            colaboradorVistas.add(new ColaboradorVista(asignacion.getColaborador()));
          }
          return colaboradorVistas;
        } else {
  
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
