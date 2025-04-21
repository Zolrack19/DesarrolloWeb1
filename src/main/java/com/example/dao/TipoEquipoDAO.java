package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.TipoEquipo;

public class TipoEquipoDAO {
    public void crearTipoEquipo(TipoEquipo tipoEquipo) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(tipoEquipo);
    t.commit();
    session.close();
  }

  public TipoEquipo obtenerTipoEquipoPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    TipoEquipo tipoEquipo = session.get(TipoEquipo.class, id);
    session.close();
    return tipoEquipo;
  }

  public void actualizarTipoEquipo(int id, TipoEquipo TipoEquipoUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    TipoEquipo TipoEquipo = session.get(TipoEquipo.class, id);
    TipoEquipo.setNombre(TipoEquipoUpdate.getNombre());

    session.update(TipoEquipo);
    t.commit();
    session.close();
  }

  public void eliminarTipoEquipo(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    session.delete(session.get(TipoEquipo.class, id));

    t.commit();
    session.close();
  }

  public void eliminarTipoEquipo(TipoEquipo tipoEquipo) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(tipoEquipo);
    t.commit();
    session.close();
  }
}
