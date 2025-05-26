package com.example.semana6.dto.solicitud;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.semana6.modelo.Solicitud;

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
