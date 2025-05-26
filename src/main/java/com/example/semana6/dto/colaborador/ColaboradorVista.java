package com.example.semana6.dto.colaborador;

import java.io.Serializable;

import com.example.semana6.modelo.Colaborador;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColaboradorVista extends ColaboradorDTO implements Serializable {

  private int id;
  private String rolColaborador;
  private String tipoDocumento;
  
  @Setter(AccessLevel.NONE)
  private String codigo;
  
  @Setter(AccessLevel.NONE)
  private String email;
  
  private short solicitudesActivas;
  private String contrasena;

  public ColaboradorVista(String numeroDocumento, String nombre, String apellidoPaterno, String apellidoMaterno) {
    super(numeroDocumento, nombre, apellidoPaterno, apellidoMaterno);
  }
  
  public ColaboradorVista(String numeroDocumento, String nombre, String apellidoPaterno, String apellidoMaterno, int id, String codigo, String email) {
    super(numeroDocumento, nombre, apellidoPaterno, apellidoMaterno);
    this.id = id;
    this.codigo = codigo;
    this.email = email;
  }

  public ColaboradorVista(int id, String rolColaborador, String tipoDocumento, String numeroDocumento, String codigo,
  String email, short solicitudesActivas, String contrasena, String nombre, String apellidoPaterno,
  String apellidoMaterno) {
    super(numeroDocumento, nombre, apellidoPaterno, apellidoMaterno);
    this.id = id;
    this.rolColaborador = rolColaborador;
    this.tipoDocumento = tipoDocumento;
    this.codigo = codigo;
    this.email = email;
    this.solicitudesActivas = solicitudesActivas;
    this.contrasena = contrasena;
  }

  @Override
  public Colaborador toColaborador() {
    Colaborador colaborador = super.toColaborador();
    colaborador.setCodigo(codigo);
    colaborador.setEmail(email);
    colaborador.setSolicitudesActivas(solicitudesActivas);
    colaborador.setContrasena(contrasena);
    return colaborador;
  }

}
