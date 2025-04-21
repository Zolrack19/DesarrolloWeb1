package com.example.dominio;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "jefe_area")
@PrimaryKeyJoinColumn(name = "id")
public class JefeArea extends Persona {
  
  @Column(name = "correo_jefe", length = 150, nullable = false)
  private String correoJefe;
  
  @Column(length = 100, nullable = false)
  String contrasena;
}
