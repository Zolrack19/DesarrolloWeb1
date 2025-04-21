package com.example.dominio;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id") 
public class Tecnico extends Persona {
  @Column(name = "cor", length = 150, nullable = false)
  String correoTecnico;
  @Column(length = 100, nullable = false)
  String contrasena;

  @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Incidencia> incidencias;

  @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<FallaDiccionario> fallasRegistradas;

}
