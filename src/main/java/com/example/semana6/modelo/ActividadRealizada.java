package com.example.semana6.modelo;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @ManyToOne
  @JoinColumn(name = "solicitud_id", nullable = false)
  private Solicitud solicitud;
  
  @ManyToOne
  @JoinColumn(name = "colaborador_id", nullable = false)
  private Colaborador colaborador;
  
  @Column(columnDefinition = "TEXT", nullable = false)
  private String descripcion;
  
  @JoinColumn(name = "hora_inicio")
  private LocalTime horaInicio;

  @JoinColumn(name = "hora_fin")
  private LocalTime horaFin;
  
  @Column(name = "fecha_emision")
  private LocalDateTime fechaEmision;

}
