package com.example.semana6.facade;

import java.util.List;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.EstadoSolicitudDAO;
import com.example.semana6.dao.SolicitudDAO;
import com.example.semana6.dao.TipoSolicitudDAO;
import com.example.semana6.dto.solicitud.SolicitudCrear;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.singleton.ValorDefecto;

public class SolicitudesFacade {
  private final SolicitudDAO solicitudDAO = new SolicitudDAO();
  private final TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
  private final EstadoSolicitudDAO estadoSolicitudDAO = new EstadoSolicitudDAO();
  private final ClienteDAO clienteDAO = new ClienteDAO();
  private final ColaboradorDAO colaboradorDAO = new ColaboradorDAO();

  public SolicitudVista crearSolicitud(SolicitudCrear solicitudCrear) {
    try {
      Solicitud solicitud = solicitudCrear.toSolicitud();
      solicitud.setTipoSolicitud(tipoSolicitudDAO.getById(solicitudCrear.getTipoSolicitudId()));
      solicitud.setEstadoSolicitud(estadoSolicitudDAO.getById(solicitudCrear.getEstadoSolicitud()));
      
      if (solicitudCrear.getClienteId() != ValorDefecto.VALOR_NULO.getValue()) {
        solicitud.setCliente(clienteDAO.getById(solicitudCrear.getClienteId()));
      }

      if (solicitudCrear.getCoordinadorId() != ValorDefecto.VALOR_NULO.getValue()) {
        solicitud.setCordinador(colaboradorDAO.getById(solicitudCrear.getCoordinadorId()));
      }
      solicitudDAO.crearSolicitud(solicitud);
      SolicitudVista solicitudVista = new SolicitudVista(solicitud);
      return solicitudVista;
    } catch (Exception e) {
      e.printStackTrace();
    }
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
