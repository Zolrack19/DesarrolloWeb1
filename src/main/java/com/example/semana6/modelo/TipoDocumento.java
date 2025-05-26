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
@Entity(name = "tipo_documento")
public class TipoDocumento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @Column(length = 120, nullable = false)
  private String nombre;

  @OneToMany(mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
  private List<Colaborador> colaboradores;
  
  @OneToMany(mappedBy = "tipoDocumento", fetch = FetchType.LAZY)
  private List<Cliente> clientes;
}
