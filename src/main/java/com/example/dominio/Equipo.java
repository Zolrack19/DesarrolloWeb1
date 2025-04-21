package com.example.dominio;
import java.time.LocalDate;
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
public class Equipo {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @JoinColumn(name = "tipo_equipo_id")
  private TipoEquipo tipoEquipo;

  @ManyToOne
  @JoinColumn(name = "estado_equipo_id")
  private EstadoEquipo estadoEquipo;
  
  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Persona usuario;

  @Column(name = "fecha_adquisicion", nullable = false)
  private LocalDate fechaAdquisicion;

  @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Incidencia> incidencias;

}
