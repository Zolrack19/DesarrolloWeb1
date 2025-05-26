package com.example.semana6.dto.colaborador;

import com.example.semana6.modelo.Colaborador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColaboradorDTO {
  private String numeroDocumento;
  private String nombre;
  private String apellidoPaterno;
  private String apellidoMaterno;
  
  public ColaboradorDTO(String numeroDocumento, String nombre, String apellidoPaterno, String apellidoMaterno) {
    this.numeroDocumento = numeroDocumento;
    this.nombre = nombre;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
  }

  public Colaborador toColaborador() {
    Colaborador colaborador = new Colaborador();
    colaborador.setNumeroDocumento(numeroDocumento);
    colaborador.setNombre(nombre);
    colaborador.setApellidoPaterno(apellidoPaterno);
    colaborador.setApellidoMaterno(apellidoMaterno);
    return colaborador;
  }

}
