package com.example.semana6.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Cliente extends Usuario {
  
  @ManyToOne
  @JoinColumn(name = "tipo_cliente_id", nullable = false)
  protected TipoCliente tipoCliente;
  
  @ManyToOne
  @JoinColumn(name = "sector_economico_id", nullable = false)
  protected SectorEconomico sectorEconomico;
  
  @Column(name = "razon_social", length = 200, nullable = false)
  private String razonSocial;

  @Column(length = 9, nullable = true)
  protected String telefono;
  
  @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
  private List<Solicitud> solicitudes;

}
