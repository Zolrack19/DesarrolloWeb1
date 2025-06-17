package com.example.semana6.dto.colaborador;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColaboradorCrear extends ColaboradorDTO implements Serializable {
  
  private short rolColaboradorId;
  private short tipoDocumentoId;

  public ColaboradorCrear(short rolColaboradorId, short tipoDocumentoId, String numeroDocumento, String nombre, String apellidoPaterno, String apellidoMaterno) {
    super(numeroDocumento, nombre, apellidoPaterno, apellidoMaterno);
    this.rolColaboradorId = rolColaboradorId;
    this.tipoDocumentoId = tipoDocumentoId;
  }
  
}
