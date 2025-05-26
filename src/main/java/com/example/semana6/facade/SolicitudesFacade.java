package com.example.semana6.facade;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.EstadoSolicitudDAO;
import com.example.semana6.dao.SolicitudDAO;
import com.example.semana6.dao.TipoSolicitudDAO;
import com.example.semana6.dto.solicitud.SolicitudCrear;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.singleton.FormatoFecha;

public class SolicitudesFacade {
  private final SolicitudDAO solicitudDAO = new SolicitudDAO();
  private final TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
  private final EstadoSolicitudDAO estadoSolicitudDAO = new EstadoSolicitudDAO();
  private final ClienteDAO clienteDAO = new ClienteDAO();
  private final ColaboradorDAO colaboradorDAO = new ColaboradorDAO();

  public SolicitudVista crearSolicitud(SolicitudCrear solicitudCrear) {
    System.out.println("entrando al facade solicitud");
    try {
      Solicitud solicitud = solicitudCrear.toSolicitud();
      solicitud.setTipoSolicitud(tipoSolicitudDAO.getById(solicitudCrear.getTipoSolicitudId()));
      solicitud.setCliente(clienteDAO.getById(solicitudCrear.getClienteId()));

      solicitud.setEstadoSolicitud(estadoSolicitudDAO.getById(solicitudCrear.getEstadoSolicitud()));

      if (solicitudCrear.getCoordinadorId() != -1) {
        solicitud.setCordinador(colaboradorDAO.getById(solicitudCrear.getCoordinadorId()));
      }
      solicitudDAO.crearSolicitud(solicitud);
      SolicitudVista solicitudVista = new SolicitudVista(solicitud.getId(), solicitud.getTipoSolicitud().getNombre(), solicitud.getTitulo(), solicitud.getDescripcion(), null,
      solicitud.getCliente().getRazonSocial(), FormatoFecha.getFormatoFecha().fechaConHora(solicitud.getFechaRegistro()), null, solicitud.getEstadoSolicitud().getNombre());
      if (solicitud.getFechaFinalizacion() != null) {
        solicitudVista.setFechaFinalizacion(FormatoFecha.getFormatoFecha().fechaConHora(solicitud.getFechaFinalizacion()));
        solicitudVista.setCoordinador(String.format("%s %s %s", solicitud.getCordinador().getNombre(), solicitud.getCordinador().getApellidoPaterno(), solicitud.getCordinador().getApellidoMaterno()));
      }
      return solicitudVista;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
