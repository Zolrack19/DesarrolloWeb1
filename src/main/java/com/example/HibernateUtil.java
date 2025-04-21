package com.example;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.dominio.FallaDiccionario;
import com.example.dominio.Equipo;
import com.example.dominio.EstadoEquipo;
import com.example.dominio.EstadoIncidencia;
import com.example.dominio.Incidencia;
import com.example.dominio.InformeTecnico;
import com.example.dominio.JefeArea;
import com.example.dominio.Persona;
import com.example.dominio.RepuestoSolicitud;
import com.example.dominio.Rol;
import com.example.dominio.Tecnico;
import com.example.dominio.TipoEquipo;

import io.github.cdimascio.dotenv.Dotenv;


public class HibernateUtil {
  private static final SessionFactory session;
  private static Dotenv dotenv = Dotenv.load();
  static {
    try {
      Configuration conf = new Configuration();
      conf.setProperty("hibernate.connection.url", dotenv.get("URL"));
      conf.setProperty("hibernate.connection.username", dotenv.get("USERNAME"));
      conf.setProperty("hibernate.connection.password", dotenv.get("PASSWORD"));
      conf.setProperty("hibernate.validator.apply_to_ddl", "false");
      conf.setProperty("hibernate.validator.autoregister_listeners", "false");
      
      conf.addAnnotatedClass(Rol.class);
      conf.addAnnotatedClass(Persona.class);
      conf.addAnnotatedClass(Tecnico.class);
      conf.addAnnotatedClass(JefeArea.class);
      conf.addAnnotatedClass(TipoEquipo.class);
      conf.addAnnotatedClass(EstadoEquipo.class);
      conf.addAnnotatedClass(EstadoIncidencia.class);
      conf.addAnnotatedClass(Equipo.class);
      conf.addAnnotatedClass(Incidencia.class);
      conf.addAnnotatedClass(InformeTecnico.class);
      conf.addAnnotatedClass(FallaDiccionario.class);
      conf.addAnnotatedClass(RepuestoSolicitud.class);

      session = conf.buildSessionFactory();
    } catch (Exception ex) {
      System.err.println("Error en inciar sesión con la base de dato " + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static SessionFactory getSession() {
    return session;
  }
}
