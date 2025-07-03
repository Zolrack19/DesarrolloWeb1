package com.example.semana6.modelo;

import java.util.List;

import com.example.semana6.dto.colaborador.ColaboradorVista;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.Setter;

@SqlResultSetMapping(name = "ColaboradorVistaMapping", classes = @ConstructorResult(
  targetClass = ColaboradorVista.class,
  columns = {
    @ColumnResult(name = "id", type = Integer.class),
    @ColumnResult(name = "rol_colaborador", type = String.class),
    @ColumnResult(name = "tipo_documento", type = String.class),
    @ColumnResult(name = "numero_documento", type = String.class),
    @ColumnResult(name = "codigo", type = String.class),
    @ColumnResult(name = "email", type = String.class),
    @ColumnResult(name = "solicitudes_activas", type = Short.class),
    @ColumnResult(name = "nombre", type = String.class),
    @ColumnResult(name = "apellido_paterno", type = String.class),
    @ColumnResult(name = "apellido_materno", type = String.class)
  }
))
@Getter
@Setter
@Entity
public class Colaborador extends Usuario {

  @ManyToOne
  @JoinColumn(name = "rol_colaborador_id", nullable = false)
  private RolColaborador rolColaborador;

  @Column(length = 50, nullable = false)
  private String codigo;

  @Column(length = 100, nullable = false)
  private String nombre;

  @Column(name = "apellido_paterno", length = 50, nullable = false)
  private String apellidoPaterno;

  @Column(name = "apellido_materno", length = 50, nullable = false)
  private String apellidoMaterno;

  @Column(name = "solicitudes_activas")
  private short solicitudesActivas;

  @OneToMany(mappedBy = "coordinador", fetch = FetchType.LAZY)
  private List<Solicitud> solicitudesCoordinador;

  @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY)
  private List<ActividadRealizada> actividadesRealizadas;

  @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY)
  private List<Asignacion> asignaciones;
}
