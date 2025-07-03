package com.example.semana6.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Asignacion {
  
  @EmbeddedId
  private AsignacionId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("solicitudId")
  @JoinColumn(name = "solicitud_id", nullable = false)
  private Solicitud solicitud;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("colaboradorId")
  @JoinColumn(name = "colaborador_id", nullable = false)
  private Colaborador colaborador;

  @Column(name = "inicio_atencion")
  private LocalDateTime inicioAtencion;
  
  @Column(name = "fin_atencion")
  private LocalDateTime finAtencion;

  public Asignacion() {}

  public Asignacion(Solicitud solicitud, Colaborador colaborador) {
    this.solicitud = solicitud;
    this.colaborador = colaborador;
    this.id = new AsignacionId(solicitud.getId(), colaborador.getId());
  }
}