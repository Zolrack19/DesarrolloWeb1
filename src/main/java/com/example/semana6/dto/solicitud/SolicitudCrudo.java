package com.example.semana6.dto.solicitud;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.semana6.modelo.Solicitud;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitudCrudo implements Serializable {
  private short tipoSolicitudId;
  private short estadoSolicitudId;
  private String titulo;
  private String descripcion;

  private int coordinadorId;
  private int clienteId;

  //datos de envío para los graficos (datos opcionales para cuando se cra una entidad)
  private int id;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime fechaRegistro;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime fechaFinalizacion;

  // constructor para crear entidad
  public SolicitudCrudo(short tipoSolicitudId, short estadoSolicitudId, String titulo, String descripcion, int coordinadorId, int clienteId) {
    this.tipoSolicitudId = tipoSolicitudId;
    this.estadoSolicitudId = estadoSolicitudId;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.coordinadorId = coordinadorId;
    this.clienteId = clienteId;
  }

  // Constructor para le mapeo con hibernate
  public SolicitudCrudo(int id, short tipoSolicitudId, short estadoSolicitudId, String titulo, String descripcion, Integer coordinadorId, Integer clienteId,
  LocalDateTime fechaRegistro, LocalDateTime fechaFinalizacion) {
    this.id = id;
    this.tipoSolicitudId = tipoSolicitudId;
    this.estadoSolicitudId = estadoSolicitudId;
    this.titulo = titulo;
    this.descripcion = descripcion;
    if (coordinadorId != null) {
      this.coordinadorId = coordinadorId;
    }
    if (clienteId != null) {
      this.clienteId = clienteId;
    }
    this.fechaRegistro = fechaRegistro;
    this.fechaFinalizacion = fechaFinalizacion;
  }

  public SolicitudCrudo(Solicitud solicitud) {
    this.id = solicitud.getId();
    this.tipoSolicitudId = solicitud.getTipoSolicitud().getId();
    this.estadoSolicitudId = solicitud.getEstadoSolicitud().getId();
    this.titulo = solicitud.getTitulo();
    this.descripcion = solicitud.getDescripcion();
    this.coordinadorId = solicitud.getCoordinador().getId();
    this.clienteId = solicitud.getCliente().getId();
    this.fechaRegistro = solicitud.getFechaRegistro();
    this.fechaFinalizacion = solicitud.getFechaFinalizacion();
  }

  public Solicitud toSolicitud() {
    Solicitud solicitud = new Solicitud();
    solicitud.setTitulo(titulo);
    solicitud.setDescripcion(descripcion);
    return solicitud;
  }

}
