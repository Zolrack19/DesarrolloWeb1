package com.example.semana6.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "persona_con_negocio")
public class PersonaConNegocio extends Cliente {
  
  @Column(length = 100, nullable = false)
  private String nombre;

  @Column(name = "apellido_paterno", length = 50, nullable = false)
  private String apellidoPaterno;  
  
  @Column(name = "apellido_materno", length = 50, nullable = false)
  private String apellidoMaterno;

}
