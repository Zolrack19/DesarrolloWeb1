package com.example.dominio;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected int id;  

  @Column(length = 200, nullable = false)
  protected String nombre;
  @Column(length = 200, nullable = false)
  protected String apellido;
  @Column(length = 100, nullable = false)
  protected String correo;
  @Column(length = 100, nullable = false)
  protected String contrasena;
  @Column(name = "numero_celular", length = 100, nullable = false)
  protected String numeroCeluar;
  
  
  @ManyToOne
  @JoinColumn(name = "rol_id", nullable = false)
  protected Rol rol;

  @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  protected List<Equipo> equipos;
  
  @OneToMany(mappedBy = "personaRegistro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  protected List<Incidencia> incidencias;
}
