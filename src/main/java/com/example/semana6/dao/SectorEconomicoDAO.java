package com.example.semana6.dao;

import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.SectorEconomico;
import com.example.semana6.singleton.HibernateUtil;

public class SectorEconomicoDAO {
  
  public void crearSectorEconomico(SectorEconomico sectorEconomico) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(sectorEconomico);

    s.getTransaction().commit();
    s.close();
  }
  
  public SectorEconomico getById(Session s, short id) {
    SectorEconomico sector = s.find(SectorEconomico.class, id);
    return sector;
  }

  public List<SectorEconomico> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<SectorEconomico> sector = s.createQuery("from SectorEconomico order by id" ,SectorEconomico.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return sector;
  }


  public void actualizarSectorEconomico(SectorEconomico sectorEconomico) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(sectorEconomico);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarSectorEconomico(SectorEconomico sectorEconomico) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(sectorEconomico);
    s.remove(sectorEconomico);

    s.getTransaction().commit();
    s.close();
  }

}
