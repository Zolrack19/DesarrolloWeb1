package com.example.dominio;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tipo_equipo")
public class TipoEquipo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private short id;

  @Column(length = 200, nullable = false)
  private String nombre;

  @OneToMany(mappedBy = "tipoEquipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  protected List<Equipo> equipos;
}
