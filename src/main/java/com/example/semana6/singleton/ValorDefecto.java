package com.example.semana6.singleton;

import lombok.Getter;

@Getter
public enum ValorDefecto {
  VALOR_NULO((short) -1),
  ESTADO_SOLICITUD((short) 1);

  private short value;
  private ValorDefecto(short value) {
    this.value = value;
  }
}