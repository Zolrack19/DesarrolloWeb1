package com.example.semana6.facade;

import org.hibernate.Session;

import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.SolicitudDAO;
import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.singleton.HibernateUtil;

public class AsignacionFacade {
  private AsignacionDAO asignacionDAO = new AsignacionDAO();
  private final SolicitudDAO solicitudDAO = new SolicitudDAO();
  private final ColaboradorDAO colaboradorDAO = new ColaboradorDAO();


  public ColaboradorVista crearAsignacion(int solicitudId, int colaboradorId) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {
      Colaborador colaborador = colaboradorDAO.getById(s, colaboradorId);
      if (colaborador == null) {
        return new ColaboradorVista("Error, el colaborador que especifica no existe");
      } else if (colaborador.getSolicitudesActivas() + 1 > 5) {
        return new ColaboradorVista("Error, el colaborador no puede ser asignado a más de 5 solicitudes activas simultáneamente");
      }

      Solicitud solicitud = solicitudDAO.getById(s, solicitudId);
      if (solicitud == null) {
        return new ColaboradorVista("Error, la solicitud que especifica para la asignación no existe");
      }

      Asignacion asignacion = new Asignacion(solicitud, colaborador);
      asignacionDAO.crearAsignacion(s, asignacion);
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

  
  public void eliminarAsignacion(Object usuario, int colaboradorId, int solicitudId) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    Asignacion asignacion = asignacionDAO.getById(s, new AsignacionId(solicitudId, colaboradorId));
    if (asignacion == null) {
      System.out.println("no hay asignación a eliminar");
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
    asignacionDAO.eliminarAsignacion(s, asignacion);
    
    s.getTransaction().commit();
    s.close();
  }
}
