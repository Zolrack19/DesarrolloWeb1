package com.example.semana6.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.TipoSolicitud;
import com.example.semana6.singleton.HibernateUtil;

public class TipoSolicitudDAO {

  public void crearTipoSolicitud(TipoSolicitud tipoSolicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(tipoSolicitud);

    s.getTransaction().commit();
    s.close();
  }
  
  public TipoSolicitud getById(short id) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    TipoSolicitud tipos = s.find(TipoSolicitud.class, id);

    s.getTransaction().commit();
    s.close();
    return tipos;
  }

  public List<TipoSolicitud> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<TipoSolicitud> tipos = s.createQuery("from TipoSolicitud order by id" ,TipoSolicitud.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return tipos;
  }


  public void actualizarTipoSolicitud(TipoSolicitud tipoSolicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(tipoSolicitud);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarTipoSolicitud(TipoSolicitud tipoSolicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(tipoSolicitud);
    s.remove(tipoSolicitud);

    s.getTransaction().commit();
    s.close();
  }

}
