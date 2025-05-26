package com.example.semana6.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Usuario {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected int id;

  @ManyToOne
  @JoinColumn(name = "tipo_documento_id", nullable = false)
  private TipoDocumento tipoDocumento;

  @Column(name = "numero_documento", length = 20, nullable = false)
  private String numeroDocumento;

  @Column(length = 100, nullable = false)
  private String email;

  @Column(length = 50, nullable = false)
  private String contrasena;

}
