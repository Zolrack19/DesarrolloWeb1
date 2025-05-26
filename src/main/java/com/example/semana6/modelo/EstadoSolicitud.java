package com.example.semana6.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "estado_solicitud")
public class EstadoSolicitud {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @Column(length = 100, nullable = false)
  private String nombre;

  @OneToMany(mappedBy = "estadoSolicitud", fetch = FetchType.LAZY)
  private List<Solicitud> solicitudes;
  
}
