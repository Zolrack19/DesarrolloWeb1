package com.example.semana6.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatoFecha {
  private static final FormatoFecha formatoFecha = new FormatoFecha();
  private final DateTimeFormatter formatter;
  
  private FormatoFecha() {
    formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", new Locale("es", "PE"));
  }
  
  public String fechaConHora(LocalDateTime fecha) {
    return fecha.format(formatter);
  }

  public static FormatoFecha getFormatoFecha() {
    return formatoFecha;
  }
}