package com.example.semana6.dto.cliente;

import com.example.semana6.modelo.Cliente;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteVista extends ClienteDTO {
  
  private int id;
  private String tipoDocumento;
  private String tipoCliente;
  private String tipoSectorEconomico;

  @Setter(AccessLevel.NONE)
  private String email;
  
  public ClienteVista(Cliente cliente) {
    super(cliente.getRazonSocial(), cliente.getNumeroDocumento(), cliente.getTelefono());
    this.id = cliente.getId();
    this.email = cliente.getEmail();
    this.tipoDocumento = cliente.getTipoDocumento().getNombre();
    this.tipoCliente = cliente.getTipoCliente().getNombre();
    this.tipoSectorEconomico = cliente.getSectorEconomico().getNombre();
  }
  
  public ClienteVista(String razonSocial, String numeroDocumento, String telefono) {
    super(razonSocial, numeroDocumento, telefono);
  }

  public ClienteVista(String razonSocial, String numeroDocumento, String telefono, int id, String email,
  String tipoDocumento, String tipoCliente, String tipoSectorEconomico) {
    super(razonSocial, numeroDocumento, telefono);
    this.id = id;
    this.email = email;
    this.tipoDocumento = tipoDocumento;
    this.tipoCliente = tipoCliente;
    this.tipoSectorEconomico = tipoSectorEconomico;
  }

  protected void llenarDatosGenerales(Cliente cliente) {
    cliente.setRazonSocial(this.getRazonSocial());
    cliente.setNumeroDocumento(this.getNumeroDocumento());
    cliente.setTelefono(this.getTelefono());
    cliente.setId(this.id);
    cliente.setEmail(this.email);
  }

  @Override
  public Cliente toCliente() {
    Cliente cliente = super.toCliente();
    cliente.setId(id); // de prueba
    cliente.setEmail(email);
    return cliente;
  }

}
