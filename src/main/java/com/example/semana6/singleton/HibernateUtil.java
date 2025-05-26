package com.example.semana6.singleton;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.semana6.modelo.ActividadRealizada;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.modelo.Cliente;
import com.example.semana6.modelo.Colaborador;
import com.example.semana6.modelo.EstadoSolicitud;
import com.example.semana6.modelo.PersonaConNegocio;
import com.example.semana6.modelo.RolColaborador;
import com.example.semana6.modelo.SectorEconomico;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.modelo.TipoCliente;
import com.example.semana6.modelo.TipoDocumento;
import com.example.semana6.modelo.TipoSolicitud;
import com.example.semana6.modelo.Usuario;

public class HibernateUtil {
  private static final SessionFactory session;

  static {
    try {
      Configuration conf = new Configuration();

      conf.addAnnotatedClass(ActividadRealizada.class);
      conf.addAnnotatedClass(Asignacion.class);
      conf.addAnnotatedClass(AsignacionId.class);
      conf.addAnnotatedClass(Cliente.class);
      conf.addAnnotatedClass(Colaborador.class);
      conf.addAnnotatedClass(EstadoSolicitud.class);
      conf.addAnnotatedClass(PersonaConNegocio.class);
      conf.addAnnotatedClass(RolColaborador.class);
      conf.addAnnotatedClass(SectorEconomico.class);
      conf.addAnnotatedClass(Solicitud.class);
      conf.addAnnotatedClass(TipoCliente.class);
      conf.addAnnotatedClass(TipoDocumento.class);
      conf.addAnnotatedClass(TipoSolicitud.class);
      conf.addAnnotatedClass(Usuario.class);

      session = conf.buildSessionFactory();
    } catch (Exception ex) {
      System.err.println("Error en inciar sesión con la base de dato " + ex);
      throw new ExceptionInInitializerError("un error  " + ex);
    }
  }

  public static SessionFactory getSession() {
    return session;
  }
}
