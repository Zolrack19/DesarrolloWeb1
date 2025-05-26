package com.example.semana6.dto.solicitud;

import java.io.Serializable;

import com.example.semana6.modelo.Solicitud;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudCrear implements Serializable {
  private short tipoSolicitudId;
  private short estadoSolicitud;
  private String titulo;
  private String descripcion;

  private int coordinadorId;
  private int clienteId;

  public SolicitudCrear() {}

  public SolicitudCrear(short tipoSolicitudId, short estadoSolicitud, String titulo, String descripcion, int coordinadorId, int clienteId) {
    this.tipoSolicitudId = tipoSolicitudId;
    this.estadoSolicitud = estadoSolicitud;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.coordinadorId = coordinadorId;
    this.clienteId = clienteId;
  }

  public Solicitud toSolicitud() {
    Solicitud solicitud = new Solicitud();
    solicitud.setTitulo(titulo);
    solicitud.setDescripcion(descripcion);
    return solicitud;
  }

}
