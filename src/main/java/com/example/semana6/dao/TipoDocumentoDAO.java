package com.example.semana6.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.TipoDocumento;
import com.example.semana6.singleton.HibernateUtil;

public class TipoDocumentoDAO {
  
  public void crearTipoDocumento(TipoDocumento tipoDocumento) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(tipoDocumento);

    s.getTransaction().commit();
    s.close();
  }
  
  public TipoDocumento getById(Session s, short id) {
    TipoDocumento documentos = s.find(TipoDocumento.class, id);
    return documentos;
  }

  public List<TipoDocumento> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<TipoDocumento> documentos = s.createQuery("from TipoDocumento order by id" ,TipoDocumento.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return documentos;
  }


  public void actualizarTipoDocumento(TipoDocumento tipoDocumento) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(tipoDocumento);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarTipoDocumento(TipoDocumento tipoDocumento) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(tipoDocumento);
    s.remove(tipoDocumento);

    s.getTransaction().commit();
    s.close();
  }
}
