package com.example.semana6.modelo;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class AsignacionId implements Serializable {
  private int solicitudId;
  private int colaboradorId;
  
  public AsignacionId() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AsignacionId)) return false;
    AsignacionId that = (AsignacionId) o;
    return solicitudId == that.solicitudId && colaboradorId == that.colaboradorId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(solicitudId, colaboradorId);
  }

}
