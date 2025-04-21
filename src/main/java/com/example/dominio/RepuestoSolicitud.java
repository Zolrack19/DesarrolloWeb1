package com.example.dominio;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "repuesto_solicitud")
public class RepuestoSolicitud {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "incidencia_id")
  private Incidencia incidencia;

  @Column(name = "nombre_repuesto", length = 200, nullable = false)
  private String nombreRepuesto;

  @Column(name = "estado_entrega", nullable = false)
  private Boolean estadoEntrega;

  @Column(name = "fecha_solicitud")
  private LocalDateTime fechaSolicitud = LocalDateTime.now();
  
  @Column(name = "fecha_entrega")
  private LocalDateTime fechaEntrega;

}
