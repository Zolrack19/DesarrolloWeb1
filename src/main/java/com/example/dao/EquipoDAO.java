package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.Equipo;

public class EquipoDAO {
  public void crearEquipo(Equipo equipo) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(equipo);
    t.commit();
    session.close();
  }
  
  public Equipo obtenerEquipoPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Equipo falla = session.get(Equipo.class, id);    
    session.close();
    return falla;
  }

  public void actualizarEquipo(int id, Equipo equipoUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    Equipo falla = session.get(Equipo.class, id);    
    falla.setEstadoEquipo(equipoUpdate.getEstadoEquipo());
    falla.setFechaAdquisicion(equipoUpdate.getFechaAdquisicion());
    falla.setTipoEquipo(equipoUpdate.getTipoEquipo());
    falla.setUsuario(equipoUpdate.getUsuario());

    session.update(falla);
    t.commit();
    session.close();
  }

  public void eliminarEquipo(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    
    session.delete(session.get(Equipo.class, id));

    t.commit();
    session.close();
  }

  public void eliminarEquipo(Equipo equipo) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(equipo);
    t.commit();
    session.close();
  }
}
