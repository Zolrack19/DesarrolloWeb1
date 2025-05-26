package com.example.semana6.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.RolColaborador;
import com.example.semana6.singleton.HibernateUtil;

public class RolColaboradorDAO {
  
  public void crearRolColaborador(RolColaborador rolColaborador) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(rolColaborador);

    s.getTransaction().commit();
    s.close();
  }
  
  public RolColaborador getById(short id) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    RolColaborador rol = s.find(RolColaborador.class, id);

    s.getTransaction().commit();
    s.close();
    return rol;
  }
  
  public List<RolColaborador> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<RolColaborador> roles = s.createQuery("from RolColaborador order by id", RolColaborador.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return roles;
  }

  public void actualizarRolColaborador(RolColaborador rolColaborador) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(rolColaborador);

    s.getTransaction().commit();
    s.close();
  }
 
  public void eliminarRolColaborador(RolColaborador rolColaborador) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(rolColaborador);
    s.remove(rolColaborador);

    s.getTransaction().commit();
    s.close();
  }
}
