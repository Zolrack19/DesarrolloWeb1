package com.example.semana6.dto.cliente;

import com.example.semana6.modelo.Cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteVista extends ClienteDTO {
  
  private int id;
  private String tipoDocumento;
  private String tipoCliente;
  private String tipoSectorEconomico;
  
  public ClienteVista(Cliente cliente) {
    super(cliente.getRazonSocial(), cliente.getNumeroDocumento(), cliente.getTelefono(), cliente.getEmail());
    this.id = cliente.getId();
    this.tipoDocumento = cliente.getTipoDocumento().getNombre();
    this.tipoCliente = cliente.getTipoCliente().getNombre();
    this.tipoSectorEconomico = cliente.getSectorEconomico().getNombre();
  }
  
  public ClienteVista(String razonSocial, String numeroDocumento, String telefono, String email) {
    super(razonSocial, numeroDocumento, telefono, email);
  }

  public ClienteVista(String razonSocial, String numeroDocumento, String telefono, int id, String email,
  String tipoDocumento, String tipoCliente, String tipoSectorEconomico) {
    super(razonSocial, numeroDocumento, telefono, email);
    this.id = id;
    this.tipoDocumento = tipoDocumento;
    this.tipoCliente = tipoCliente;
    this.tipoSectorEconomico = tipoSectorEconomico;
  }

  protected void llenarDatosGenerales(Cliente cliente) {
    cliente.setRazonSocial(this.getRazonSocial());
    cliente.setNumeroDocumento(this.getNumeroDocumento());
    cliente.setTelefono(this.getTelefono());
    cliente.setId(this.id);
  }

  @Override
  public Cliente toCliente() {
    Cliente cliente = super.toCliente();
    cliente.setId(id); // de prueba
    return cliente;
  }

}
