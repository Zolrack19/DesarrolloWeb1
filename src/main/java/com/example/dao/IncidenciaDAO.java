package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.Incidencia;

public class IncidenciaDAO {
    
  public void crearIncidencia(Incidencia incidencia) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(incidencia);
    t.commit();
    session.close();
  }
  
  public Incidencia obtenerIncidenciaPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Incidencia incidencia = session.get(Incidencia.class, id);    
    session.close();
    return incidencia;
  }

  public void actualizarIncidencia(int id, Incidencia incidenciaUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    Incidencia incidencia = session.get(Incidencia.class, id);    
    incidencia.setDescripcion(incidenciaUpdate.getDescripcion());
    incidencia.setEquipo(incidenciaUpdate.getEquipo());
    incidencia.setEstadoIncidencia(incidenciaUpdate.getEstadoIncidencia());
    incidencia.setFechaRegistro(incidenciaUpdate.getFechaRegistro());
    incidencia.setPersonaRegistro(incidenciaUpdate.getPersonaRegistro());
    incidencia.setTecnico(incidenciaUpdate.getTecnico());

    session.update(incidencia);
    t.commit();
    session.close();
  }

  public void eliminarIncidencia(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    
    session.delete(session.get(Incidencia.class, id));

    t.commit();
    session.close();
  }

  public void eliminarIncidencia(Incidencia incidencia) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(incidencia);
    t.commit();
    session.close();
  }
}
