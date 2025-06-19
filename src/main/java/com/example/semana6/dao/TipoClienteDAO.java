package com.example.semana6.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.TipoCliente;
import com.example.semana6.singleton.HibernateUtil;

public class TipoClienteDAO {
  public void crearTipoCliente(TipoCliente tipoCliente) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(tipoCliente);

    s.getTransaction().commit();
    s.close();
  }
  
  public TipoCliente getById(Session s, short id) {
    TipoCliente tipoCliente = s.find(TipoCliente.class, id);
    return tipoCliente;
  }

  public List<TipoCliente> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<TipoCliente> tipoClientes = s.createQuery("from TipoCliente order by id" ,TipoCliente.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return tipoClientes;
  }


  public void actualizarTipoCliente(TipoCliente tipoCliente) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(tipoCliente);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarTipoCliente(TipoCliente tipoCliente) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(tipoCliente);
    s.remove(tipoCliente);

    s.getTransaction().commit();
    s.close();
  }

}
