package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.FallaDiccionario;

public class FallaDiccionarioDAO {
  
  public void registrarFalla(FallaDiccionario falla) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(falla);
    t.commit();
    session.close();
  }
  
  public FallaDiccionario obtenerFallaPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    FallaDiccionario falla = session.get(FallaDiccionario.class, id);    
    session.close();
    return falla;
  }

  public void actualizarFalla(int id, FallaDiccionario fallaUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    FallaDiccionario falla = session.get(FallaDiccionario.class, id);    
    falla.setFechaRegitro(fallaUpdate.getFechaRegitro());
    falla.setNombreFalla(fallaUpdate.getNombreFalla());
    falla.setSolucionRegitrada(fallaUpdate.getSolucionRegitrada());
    falla.setTecnico(fallaUpdate.getTecnico());

    session.update(falla);
    t.commit();
    session.close();
  }

  public void eliminarRegistroFalla(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    
    session.delete(session.get(FallaDiccionario.class, id));

    t.commit();
    session.close();
  }

  public void eliminarRegistroFalla(FallaDiccionario falla) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(falla);
    t.commit();
    session.close();
  }
}
