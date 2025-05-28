package com.example.semana6.singleton;

import lombok.Getter;

@Getter
public enum ValorDefecto {
  VALOR_NULO(-1),
  ESTADO_SOLICITUD( 1);

  private int value;
  private ValorDefecto(int value) {
    this.value = value;
  }
}