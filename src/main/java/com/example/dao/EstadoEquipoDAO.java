package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.EstadoEquipo;

public class EstadoEquipoDAO {
  
    public void crearEstadoEquipo(EstadoEquipo estadoEquipo) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(estadoEquipo);
    t.commit();
    session.close();
  }
  
  public EstadoEquipo obtenerEstadoEquipoPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    EstadoEquipo estadoEquipo = session.get(EstadoEquipo.class, id);    
    session.close();
    return estadoEquipo;
  }

  public void actualizarEstadoEquipo(int id, EstadoEquipo EstadoEquipoUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    EstadoEquipo estadoEquipo = session.get(EstadoEquipo.class, id);    
    estadoEquipo.setNombre(EstadoEquipoUpdate.getNombre());

    session.update(estadoEquipo);
    t.commit();
    session.close();
  }

  public void eliminarEstadoEquipo(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    
    session.delete(session.get(EstadoEquipo.class, id));

    t.commit();
    session.close();
  }

  public void eliminarEstadoEquipo(EstadoEquipo EstadoEquipo) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(EstadoEquipo);
    t.commit();
    session.close();
  }
}
