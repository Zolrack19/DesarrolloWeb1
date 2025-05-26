package com.example.semana6.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tipo_cliente")
public class TipoCliente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @Column(length = 100, nullable = false)
  private String nombre;

  @OneToMany(mappedBy = "tipoCliente")
  private List<Cliente> clientes;

}
