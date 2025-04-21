package com.example.dominio;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "informe_tecnico")
public class InformeTecnico {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "incidencia_id", referencedColumnName = "id")
  private Incidencia incidencia;

  @Column(name = "descripcion_solucion", columnDefinition = "TEXT", nullable = false)
  private String descipcionSolucion;

  @Column(name = "fecha_emision", nullable = false)
  private LocalDateTime fechaEmision = LocalDateTime.now();

}
