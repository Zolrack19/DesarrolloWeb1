package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.InformeTecnico;

public class InformeTecnicoDAO {
  
  public void crearInformeTecnico(InformeTecnico informeTecnico) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(informeTecnico);
    t.commit();
    session.close();
  }
  
  public InformeTecnico obtenerInformeTecnicoPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    InformeTecnico informeTecnico = session.get(InformeTecnico.class, id);    
    session.close();
    return informeTecnico;
  }

  public void actualizarInformeTecnico(int id, InformeTecnico informeTecnicoUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    InformeTecnico informeTecnico = session.get(InformeTecnico.class, id);    
    informeTecnico.setDescipcionSolucion(informeTecnicoUpdate.getDescipcionSolucion());
    informeTecnico.setFechaEmision(informeTecnicoUpdate.getFechaEmision());
    informeTecnico.setIncidencia(informeTecnicoUpdate.getIncidencia());

    session.update(informeTecnico);
    t.commit();
    session.close();
  }

  public void eliminarInformeTecnico(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    
    session.delete(session.get(InformeTecnico.class, id));

    t.commit();
    session.close();
  }

  public void eliminarInformeTecnico(InformeTecnico informeTecnico) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(informeTecnico);
    t.commit();
    session.close();
  }
}
