package com.example.dominio;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "diccionario_falla")
public class FallaDiccionario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 200, nullable = false)
  private String nombreFalla;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String solucionRegitrada;

  @ManyToOne
  @JoinColumn(name = "tecnico_id")
  private Tecnico tecnico;

  @Column(name = "fecha_registro")
  private LocalDate fechaRegitro = LocalDate.now();

}
