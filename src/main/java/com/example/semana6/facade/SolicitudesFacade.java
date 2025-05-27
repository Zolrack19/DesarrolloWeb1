package com.example.semana6.facade;

import java.util.ArrayList;
import java.util.List;

import com.example.semana6.dao.ClienteDAO;
import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.EstadoSolicitudDAO;
import com.example.semana6.dao.SolicitudDAO;
import com.example.semana6.dao.TipoSolicitudDAO;
import com.example.semana6.dto.solicitud.SolicitudCrear;
import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.modelo.Solicitud;

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
      solicitud.setCliente(clienteDAO.getById(solicitudCrear.getClienteId()));

      solicitud.setEstadoSolicitud(estadoSolicitudDAO.getById(solicitudCrear.getEstadoSolicitud()));

      if (solicitudCrear.getCoordinadorId() != -1) {
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

  public List<SolicitudVista> getSolicitudes(int idCliente, int inicio, int fin) {
    List<Solicitud> solicitudes = solicitudDAO.getByClienteId(idCliente, inicio, fin);
    if (solicitudes.size() == 0) return null;
    List<SolicitudVista> solicitudVistas = new ArrayList<>();
    solicitudes.forEach((solicitud) -> {
      solicitudVistas.add(new SolicitudVista(solicitud));
    });
    return solicitudVistas;
  }

  public List<SolicitudVista> getSolicitudes(int inicio, int fin) {
    List<Solicitud> solicitudes = solicitudDAO.getRango(inicio, fin);
    if (solicitudes.size() == 0) return null;
    List<SolicitudVista> solicitudVistas = new ArrayList<>();
    solicitudes.forEach((solicitud) -> {
      solicitudVistas.add(new SolicitudVista(solicitud));
    });
    return solicitudVistas;
  }
}
