package com.example.semana6.dto.solicitud;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.semana6.modelo.Solicitud;
import com.example.semana6.singleton.FormatoFecha;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudVista implements Serializable {
  
  private int id;
  private String tipoSolicitud;
  private String titulo;
  private String descripcion;
  private String coordinador;
  private String cliente;

  @Setter(AccessLevel.NONE)
  private String fechaRegistro;

  private String fechaFinalizacion;

  @Setter(AccessLevel.NONE)
  private String estadoSolicitud;

  public SolicitudVista() {}

  public SolicitudVista(Solicitud solicitud) {
    this.id = solicitud.getId();
    this.tipoSolicitud = solicitud.getTipoSolicitud().getNombre();
    this.titulo = solicitud.getTitulo();
    this.descripcion = solicitud.getDescripcion();
    if (solicitud.getCordinador() != null) {
      this.coordinador = String.format("%s %s %s", solicitud.getCordinador().getNombre(), solicitud.getCordinador().getApellidoPaterno(), solicitud.getCordinador().getApellidoMaterno());
    }
    if (solicitud.getCliente() != null) {
      this.cliente = solicitud.getCliente().getRazonSocial();
    }
    this.fechaRegistro = FormatoFecha.getFormatoFecha().fechaConHora(solicitud.getFechaRegistro());
    if (fechaFinalizacion != null) {
      this.fechaFinalizacion = FormatoFecha.getFormatoFecha().fechaConHora(solicitud.getFechaFinalizacion());
    }
    this.estadoSolicitud = solicitud.getEstadoSolicitud().getNombre();
  }

  public SolicitudVista(int id, String tipoSolicitud, String titulo, String descripcion, String coordinador, String cliente,
  String fechaRegistro, String fechaFinalizacion, String estadoSolicitud) {
    this.id = id;
    this.tipoSolicitud = tipoSolicitud;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.coordinador = coordinador;
    this.cliente = cliente;
    this.fechaRegistro = fechaRegistro;
    this.fechaFinalizacion = fechaFinalizacion;
    this.estadoSolicitud = estadoSolicitud;
  }


  public Solicitud toSolicitud() {
    Solicitud solicitud = new Solicitud();
    solicitud.setTitulo(titulo);
    solicitud.setDescripcion(descripcion);
    //tal vez lo borre
    solicitud.setFechaRegistro(LocalDateTime.parse(fechaRegistro));
    solicitud.setFechaFinalizacion(fechaFinalizacion != null ? LocalDateTime.parse(fechaFinalizacion) : null);
    return solicitud;
  }
}
