package com.example.semana6.dto.cliente;

import com.example.semana6.modelo.Cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCrear extends ClienteDTO {
  private short tipoDocumentoId;
  private short tipoClienteId;
  private short sectorEconomicoId;
  private String email;
  private String contrasena;

  public ClienteCrear(String razonSocial, String numeroDocumento, String telefono) {
    super(razonSocial, numeroDocumento, telefono);
  }

  public ClienteCrear(short tipoDocumentoId, short tipoClienteId, short sectorEconomicoId, String razonSocial,
  String numeroDocumento, String email, String contrasena, String telefono) {
    super(razonSocial, numeroDocumento, telefono);
    this.email = email;
    this.contrasena = contrasena;
    this.tipoDocumentoId = tipoDocumentoId;
    this.tipoClienteId = tipoClienteId;
    this.sectorEconomicoId = sectorEconomicoId;
  }
  
  protected void llenarDatosGenerales(Cliente cliente) {
    cliente.setRazonSocial(this.getRazonSocial());
    cliente.setNumeroDocumento(this.getNumeroDocumento());
    cliente.setContrasena(this.getContrasena());
    cliente.setTelefono(this.getTelefono());
    cliente.setEmail(this.email);
    cliente.setContrasena(this.contrasena);
  }

  @Override
  public Cliente toCliente() {
    Cliente cliente = super.toCliente();
    cliente.setEmail(email);
    cliente.setContrasena(contrasena);
    return cliente;
  }


}
