package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.Tecnico;

public class TecnicoDAO {
  
  public void crearTecnico(Tecnico tecnico) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(tecnico);
    t.commit();
    session.close();
  }

  public Tecnico obtenerTecnicoPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Tecnico tecnico = session.get(Tecnico.class, id);
    session.close();
    return tecnico;
  }

  public void actualizarTecnico(int id, Tecnico tecnicoUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    Tecnico tecnico = session.get(Tecnico.class, id);
    tecnico.setNombre(tecnicoUpdate.getNombre());
    tecnico.setApellido(tecnicoUpdate.getApellido());
    tecnico.setCorreo(tecnicoUpdate.getCorreo());
    tecnico.setCorreoTecnico(tecnicoUpdate.getCorreoTecnico());
    tecnico.setNumeroCeluar(tecnicoUpdate.getNumeroCeluar());

    session.update(tecnico);
    t.commit();
    session.close();
  }

  public void eliminarTecnico(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    session.delete(session.get(Tecnico.class, id));

    t.commit();
    session.close();
  }

  public void eliminarTecnico(Tecnico tecnico) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(tecnico);
    t.commit();
    session.close();
  }
}
