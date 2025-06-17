package com.example.semana6.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatoFecha {
  private static final DateTimeFormatter formatter;
  
  static {
    formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.of("es", "PE"));
  }
  
  public static String fechaConHora(LocalDateTime fecha) {
    return fecha.format(formatter);
  }

}