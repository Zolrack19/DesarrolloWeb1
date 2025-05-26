package com.example.semana6.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.EstadoSolicitud;
import com.example.semana6.singleton.HibernateUtil;

public class EstadoSolicitudDAO {
  
  public void crearEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(estadoSolicitud);

    s.getTransaction().commit();
    s.close();
  }
  
  public EstadoSolicitud getById(short id) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    EstadoSolicitud estados = s.find(EstadoSolicitud.class, id);

    s.getTransaction().commit();
    s.close();
    return estados;
  }

  public List<EstadoSolicitud> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<EstadoSolicitud> estados = s.createQuery("from EstadoSolicitud order by id" ,EstadoSolicitud.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return estados;
  }


  public void actualizarEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(estadoSolicitud);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(estadoSolicitud);
    s.remove(estadoSolicitud);

    s.getTransaction().commit();
    s.close();
  }
}
