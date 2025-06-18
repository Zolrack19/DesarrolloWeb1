package com.example.semana6.dto.asignacion;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.semana6.modelo.Asignacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignacionCrear implements Serializable {
  private int solicitudId;
  private int colaboradorId;
  private String inicioAtencion;
  private String finAtencion;
  
  public AsignacionCrear(int solicitudId, int colaboradorId) {
    this.solicitudId = solicitudId;
    this.colaboradorId = colaboradorId;
  }

  public AsignacionCrear(int solicitudId, int colaboradorId, String inicioAtencion, String finAtencion) {
    this.solicitudId = solicitudId;
    this.colaboradorId = colaboradorId;
    this.inicioAtencion = inicioAtencion;
    this.finAtencion = finAtencion;
  }

  public Asignacion toAsignacion() {
    Asignacion asignacion = new Asignacion();
    asignacion.setInicioAtencion(LocalDateTime.parse(inicioAtencion));
    asignacion.setFinAtencion(LocalDateTime.parse(finAtencion));
    return asignacion;
  }

}
