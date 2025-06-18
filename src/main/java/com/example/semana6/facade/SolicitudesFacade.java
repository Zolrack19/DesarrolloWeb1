package com.example.semana6.facade;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.EstadoSolicitudDAO;
import com.example.semana6.dao.SolicitudDAO;
import com.example.semana6.dao.TipoSolicitudDAO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.dto.solicitud.SolicitudCrear;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.singleton.HibernateUtil;
import com.example.semana6.singleton.ValorDefecto;

public class SolicitudesFacade {
  private final SolicitudDAO solicitudDAO = new SolicitudDAO();
  private final AsignacionDAO asignacionDAO = new AsignacionDAO();
  private final TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
  private final EstadoSolicitudDAO estadoSolicitudDAO = new EstadoSolicitudDAO();
  private final ClienteDAO clienteDAO = new ClienteDAO();
  private final ColaboradorDAO colaboradorDAO = new ColaboradorDAO();

  public SolicitudVista crearSolicitud(SolicitudCrear solicitudCrear) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {
      
      Solicitud solicitud = solicitudCrear.toSolicitud();
      solicitud.setTipoSolicitud(tipoSolicitudDAO.getById(s, solicitudCrear.getTipoSolicitudId()));
      solicitud.setEstadoSolicitud(estadoSolicitudDAO.getById(s, solicitudCrear.getEstadoSolicitud()));
      
      if (solicitudCrear.getClienteId() != ValorDefecto.VALOR_NULO.getValue()) {
        solicitud.setCliente(clienteDAO.getById(s, solicitudCrear.getClienteId()));
      }

      if (solicitudCrear.getCoordinadorId() != ValorDefecto.VALOR_NULO.getValue()) {
        solicitud.setCordinador(colaboradorDAO.getById(s, solicitudCrear.getCoordinadorId()));
      }
      solicitudDAO.crearSolicitud(solicitud);
      SolicitudVista solicitudVista = new SolicitudVista(solicitud);
      
      s.getTransaction().commit();
      s.close();
      return solicitudVista;
    } catch (Exception e) {
      s.getTransaction().rollback();
      e.printStackTrace();
    }
    s.close();
    return null;
  }

  public void asignarCoordinadorASolicitud(int solicitudId, int colaboradorId) {

  }

  public ColaboradorVista asignarColaboradorASolicitud(int solicitudId, int colaboradorId) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    try {
      Solicitud solicitud = solicitudDAO.getById(s, solicitudId);
      Colaborador colaborador = colaboradorDAO.getById(s, colaboradorId);
      Asignacion asignacion = new Asignacion(solicitud, colaborador);
      asignacionDAO.crearAsignacion(s, asignacion);
      
      s.getTransaction().commit();
      s.close();
      return new ColaboradorVista(colaborador);
    } catch (Exception e) {
      s.getTransaction().rollback();
      e.printStackTrace();
    }
    s.close();
    return null;
  }

  public List<SolicitudVista> getSolicitudes(int clienteId, int numPag) {
    List<SolicitudVista> solicitudes = solicitudDAO.getByClienteId(clienteId, (numPag - 1)*10, 10);
    if (solicitudes.size() == 0) return null;
    return solicitudes;
  }

  public List<SolicitudVista> getSolicitudes(int numPag) {
    List<SolicitudVista> solicitudes = solicitudDAO.getRangoVista((numPag - 1)*10, 10);
    if (solicitudes.size() == 0) return null;
    return solicitudes;
  }
}
