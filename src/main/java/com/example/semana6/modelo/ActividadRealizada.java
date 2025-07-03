package com.example.semana6.modelo;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter//
@Entity(name = "actividad_realizada")
public class ActividadRealizada {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "solicitud_id", nullable = false)
  private Solicitud solicitud;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "colaborador_id", nullable = false)
  private Colaborador colaborador;
  
  @Column(columnDefinition = "TEXT", nullable = false)
  private String descripcion;
  
  @Column(name = "hora_inicio")
  private LocalTime horaInicio;

  @Column(name = "hora_fin")
  private LocalTime horaFin;
  
  @org.hibernate.annotations.Generated(org.hibernate.annotations.GenerationTime.INSERT)
  @Column(name = "fecha_emision", insertable = false, nullable = false)
  private LocalDateTime fechaEmision;

}
