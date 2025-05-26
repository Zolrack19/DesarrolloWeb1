package com.example.semana6.dto.actividadRealizada;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.semana6.modelo.ActividadRealizada;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActividadRealizadaVista {
  private String solicitud;
  private String colaborador;

  private String descripcion;
  private String horaInicio;
  private String horaFin;
  private String fechaEmision;
  
  public ActividadRealizadaVista() {}
  
  public ActividadRealizadaVista(String solicitud, String colaborador, String descripcion, String horaInicio,
  String horaFin, String fechaEmision) {
    this.solicitud = solicitud;
    this.colaborador = colaborador;
    this.descripcion = descripcion;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.fechaEmision = fechaEmision;
  }

  public ActividadRealizada toActividadRealizada() {
    ActividadRealizada actividadRealizada = new ActividadRealizada();
    actividadRealizada.setDescripcion(descripcion);
    actividadRealizada.setHoraInicio(LocalTime.parse(horaInicio));
    actividadRealizada.setHoraFin(LocalTime.parse(horaFin));
    actividadRealizada.setFechaEmision(LocalDateTime.parse(fechaEmision));
    return actividadRealizada;
  }

}
