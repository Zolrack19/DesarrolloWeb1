package com.example.semana6.dto.cliente;

import com.example.semana6.modelo.PersonaConNegocio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaConNegocioCrear extends ClienteCrear {
  private String nombre;
  private String apellidoPaterno;
  private String apellidoMaterno;
  
  public PersonaConNegocioCrear(String razonSocial, String numeroDocumento, String telefono, String email) {
    super(razonSocial, numeroDocumento, telefono, email);
  }
  
  public PersonaConNegocioCrear(String razonSocial, String numeroDocumento, String telefono, String email,
  String nombre, String apellidoPaterno, String apellidoMaterno) {
    super(razonSocial, numeroDocumento, telefono, email);
    this.nombre = nombre;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
  }

  public PersonaConNegocioCrear(short tipoDocumentoId, short tipoClienteId, short tipoSectorEconomico, String razonSocial,
  String numeroDocumento, String email, String contrasena, String telefono) {
    super(tipoDocumentoId, tipoClienteId, tipoSectorEconomico, razonSocial, numeroDocumento, email, contrasena, telefono);
  }

  public PersonaConNegocioCrear(short tipoDocumentoId, short tipoClienteId, short tipoSectorEconomico, String razonSocial,
  String numeroDocumento, String email, String contrasena, String telefono, String nombre, String apellidoPaterno,
  String apellidoMaterno) {
    super(tipoDocumentoId, tipoClienteId, tipoSectorEconomico, razonSocial, numeroDocumento, email, contrasena, telefono);
    this.nombre = nombre;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
  }

  @Override
  public PersonaConNegocio toCliente() {
    PersonaConNegocio cliente = new PersonaConNegocio();
    llenarDatosGenerales(cliente);
    cliente.setNombre(nombre);
    cliente.setApellidoPaterno(apellidoPaterno);
    cliente.setApellidoMaterno(apellidoMaterno);
    return cliente;
  }

}
