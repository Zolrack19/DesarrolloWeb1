package com.example.semana6.modelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Solicitud {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 200, nullable = false)
  private String titulo;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String descripcion;
  
  @ManyToOne
  @JoinColumn(name = "coordinador_id")
  private Colaborador cordinador;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @org.hibernate.annotations.Generated(org.hibernate.annotations.GenerationTime.INSERT)
  @Column(name = "fecha_registro", insertable = false, nullable = false)
  private LocalDateTime fechaRegistro;
  
  @Column(name = "fecha_finalizacion")
  private LocalDateTime fechaFinalizacion;

  @ManyToOne
  @JoinColumn(name = "tipo_solicitud_id", nullable = false)
  private TipoSolicitud tipoSolicitud;

  @ManyToOne
  @JoinColumn(name = "estado_solicitud_id", nullable = false)
  private EstadoSolicitud estadoSolicitud;

  @OneToMany(mappedBy = "solicitud", fetch = FetchType.LAZY)
  private List<ActividadRealizada> actividadesRealizadas;
  
  @OneToMany(mappedBy = "solicitud", fetch = FetchType.LAZY)
  private List<Asignacion> asignaciones;
}
