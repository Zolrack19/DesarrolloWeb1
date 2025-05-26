package com.example.semana6.dto.actividadRealizada;

import java.time.LocalTime;

import com.example.semana6.modelo.ActividadRealizada;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActividadRealizadaCrear {
  private int solicitudId;
  private int colaboradorId;

  private String descripcion;
  private String horaInicio;
  private String horaFin;
  
  public ActividadRealizadaCrear() {}
  
  public ActividadRealizadaCrear(int solicitudId, int colaboradorId, String descripcion, String horaInicio,
  String horaFin) {
    this.solicitudId = solicitudId;
    this.colaboradorId = colaboradorId;
    this.descripcion = descripcion;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
  }

  public ActividadRealizada toActividadRealizada() {
    ActividadRealizada actividadRealizada = new ActividadRealizada();
    actividadRealizada.setDescripcion(descripcion);
    actividadRealizada.setHoraInicio(LocalTime.parse(horaInicio));
    actividadRealizada.setHoraFin(LocalTime.parse(horaFin));
    return actividadRealizada;
  }

}
