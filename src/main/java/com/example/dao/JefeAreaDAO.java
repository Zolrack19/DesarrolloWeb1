package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.JefeArea;

public class JefeAreaDAO {

  public void crearJefeArea(JefeArea jefeArea) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(jefeArea);
    t.commit();
    session.close();
  }

  public JefeArea obtenerJefeAreaPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    JefeArea jefeArea = session.get(JefeArea.class, id);
    session.close();
    return jefeArea;
  }

  public void actualizarJefeArea(int id, JefeArea jefeAreaUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    JefeArea jefeArea = session.get(JefeArea.class, id);
    jefeArea.setApellido(jefeAreaUpdate.getApellido());
    jefeArea.setContrasena(jefeAreaUpdate.getContrasena());
    jefeArea.setCorreo(jefeAreaUpdate.getCorreo());
    jefeArea.setCorreoJefe(jefeAreaUpdate.getCorreoJefe());
    jefeArea.setNombre(jefeAreaUpdate.getNombre());
    jefeArea.setNumeroCeluar(jefeAreaUpdate.getNumeroCeluar());

    session.update(jefeArea);
    t.commit();
    session.close();
  }

  public void eliminarJefeArea(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    session.delete(session.get(JefeArea.class, id));

    t.commit();
    session.close();
  }

  public void eliminarJefeArea(JefeArea jefeArea) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(jefeArea);
    t.commit();
    session.close();
  }
}
