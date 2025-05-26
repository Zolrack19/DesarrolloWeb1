package com.example.semana6.modelo;

import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @Entity
public class Empresa extends Cliente {
  @Column(name = "razon_social", length = 200, nullable = false)
  private String razonSocial;
}
