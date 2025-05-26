package com.example.semana6.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Colaborador extends Usuario {
  
  @ManyToOne
  @JoinColumn(name = "rol_colaborador_id", nullable = false)
  private RolColaborador rolColaborador;
  
  @Column(length = 50, nullable = false)
  private String codigo;
  
  @Column(length = 100, nullable = false)
  private String nombre;

  @Column(name = "apellido_paterno", length = 50, nullable = false)
  private String apellidoPaterno;
  
  @Column(name = "apellido_materno", length = 50, nullable = false)
  private String apellidoMaterno;
  
  @Column(name = "solicitudes_activas")
  private short solicitudesActivas;

  @OneToMany(mappedBy = "cordinador", fetch = FetchType.LAZY)
  private List<Solicitud> solicitudesCordinador;

  @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY)
  private List<ActividadRealizada> actividadesRealizadas;
  
  @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY)
  private List<Asignacion> asignaciones;
}
