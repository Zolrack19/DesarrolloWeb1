package com.example.dominio;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Incidencia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "equipo_id")
  private Equipo equipo;
  
  @Column(length = 2000, nullable = false)
  private String descripcion;
  
  @Column(name = "fecha_registro", nullable = false)
  private LocalDateTime fechaRegistro = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "persona_registro_id")
  private Persona personaRegistro;
  
  @ManyToOne
  @JoinColumn(name = "estado_incidencia_id")
  private EstadoIncidencia estadoIncidencia;

  @ManyToOne
  @JoinColumn(name = "tecnico_id", nullable = false)
  private Tecnico tecnico;

  @OneToMany(mappedBy = "incidencia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<RepuestoSolicitud> repuestoSolicitudes;
  
}
