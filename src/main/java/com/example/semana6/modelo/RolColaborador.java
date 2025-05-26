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
@Entity(name = "rol_colaborador")
public class RolColaborador {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @Column(length = 120, nullable = false)
  private String nombre;
  
  @Column(name = "prefijo_codigo", length = 10, nullable = false)
  private String prefijoCodigo;
  
  @Column(name = "nombre_secuencia", length = 130, nullable = false)
  private String nombreSecuencia;

  @OneToMany(mappedBy = "rolColaborador")
  private List<Colaborador> colaboradores;

}
 