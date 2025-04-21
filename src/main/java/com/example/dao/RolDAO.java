package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.Rol;

public class RolDAO {
  
  public void crearRol(Rol rol) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(rol);
    t.commit();
    session.close();
  }

  public Rol obtenerRolPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Rol Rol = session.get(Rol.class, id);
    session.close();
    return Rol;
  }

  public void actualizarRol(int id, Rol rolUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    Rol rol = session.get(Rol.class, id);
    rol.setNombre(rolUpdate.getNombre());

    session.update(rol);
    t.commit();
    session.close();
  }

  public void eliminarRol(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    session.delete(session.get(Rol.class, id));

    t.commit();
    session.close();
  }

  public void eliminarRol(Rol rol) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(rol);
    t.commit();
    session.close();
  }
}
