package com.example.semana6.dto.cliente;

import com.example.semana6.modelo.PersonaConNegocio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaConNegocioVista extends ClienteVista {
  private String nombre;
  private String apellidoPaterno;
  private String apellidoMaterno;

  public PersonaConNegocioVista(String razonSocial, String numeroDocumento, String telefono) {
    super(razonSocial, numeroDocumento, telefono);
  }

  public PersonaConNegocioVista(String razonSocial, String numeroDocumento, String telefono, String nombre,
  String apellidoPaterno, String apellidoMaterno) {
    super(razonSocial, numeroDocumento, telefono);
    this.nombre = nombre;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
  }

  public PersonaConNegocioVista(String razonSocial, String numeroDocumento, String telefono, int id, String email,
  String tipoDocumento, String tipoCliente, String tipoSectorEconomico) {
    super(razonSocial, numeroDocumento, telefono, id, email, tipoDocumento, tipoCliente, tipoSectorEconomico);
  }
  
  public PersonaConNegocioVista(String razonSocial, String numeroDocumento, String telefono, int id, String email,
  String tipoDocumento, String tipoCliente, String tipoSectorEconomico, String nombre, String apellidoPaterno,
  String apellidoMaterno) {
    super(razonSocial, numeroDocumento, telefono, id, email, tipoDocumento, tipoCliente, tipoSectorEconomico);
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
