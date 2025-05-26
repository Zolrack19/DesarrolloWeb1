package com.example.semana6.dto.cliente;

import java.io.Serializable;

import com.example.semana6.modelo.Cliente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO implements Serializable {
  private String razonSocial;
  private String numeroDocumento;
  private String telefono;

  public ClienteDTO(String razonSocial, String numeroDocumento, String telefono) {
    this.razonSocial = razonSocial;
    this.numeroDocumento = numeroDocumento;
    this.telefono = telefono;
  }

  public Cliente toCliente() {
    Cliente cliente = new Cliente();
    cliente.setRazonSocial(razonSocial);
    cliente.setNumeroDocumento(numeroDocumento);
    cliente.setTelefono(telefono);
    return cliente;
  }
  
}
