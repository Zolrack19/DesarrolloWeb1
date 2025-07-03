package com.example.semana6.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.ActividadRealizada;
import com.example.semana6.singleton.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ActividadRealizadaDAO {
  
  public void crearActividad(Session s, ActividadRealizada actividad) {
    s.persist(actividad);
    s.flush();
    s.refresh(actividad);
    
  }

  public ActividadRealizada getById(long id) {
    Session s =  HibernateUtil.getSession().openSession();

    ActividadRealizada actividad = s.find(ActividadRealizada.class, id);

    s.close();
    return actividad;
  }
  
  public List<ActividadRealizada> getByIdSolicitud(int idSolicitud, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();

    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.solicitud.id = :id order by a.id", ActividadRealizada.class)
    .setParameter("id", idSolicitud)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return actividades;
  }
  
  public List<ActividadRealizada> getByIdColaborador(int idColaborador, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();

    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.colaborador.id = :id order by a.id", ActividadRealizada.class)
    .setParameter("id", idColaborador)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return actividades;
  }

  public List<ActividadRealizada> getByIdColaboradorSolicitud(Session s, int idColaborador, int idSolicitud) {
    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.colaborador.id = :idC and a.solicitud.id = :idS order by a.id", ActividadRealizada.class)
    .setParameter("idC", idColaborador)
    .setParameter("idS", idSolicitud)
    .list();
    return actividades;
  }

  public List<ActividadRealizada> getByIdColaboradorSolicitud(Session s, int idColaborador, int idSolicitud, int inicio, int fin) {
    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.colaborador.id = :idC and a.solicitud.id = :idS order by a.id", ActividadRealizada.class)
    .setParameter("idC", idColaborador)
    .setParameter("idS", idSolicitud)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    return actividades;
  }

  public List<ActividadRealizada> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();

    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada order by id", ActividadRealizada.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return actividades;
  }

  public List<ActividadRealizada> getRangoByFechaEmision(LocalDateTime fecha, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.fechaEmision = :fecha order by a.id"
    , ActividadRealizada.class)
    .setParameter("fecha", fecha)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return actividades;
  }

  public List<ActividadRealizada> getRangoByFechaEmision(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.fechaEmision between :inicio and :fin order by a.id"
    , ActividadRealizada.class)
    .setParameter("inicio", fechaInicio)
    .setParameter("fin", fechaFin)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return actividades;
  }

  public List<ActividadRealizada> getRangoByFechaEmision(long id, LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<ActividadRealizada> actividades = s.createQuery("from actividad_realizada a where a.id = :id and a.fechaEmision between :inicio and :fin order by a.id"
    , ActividadRealizada.class)
    .setParameter("id", id)
    .setParameter("inicio", fechaInicio)
    .setParameter("fin", fechaFin)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return actividades;
  }

  
  public void actualizarActividad(ActividadRealizada actividad) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(actividad);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarActividad(ActividadRealizada actividad) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(actividad);
    s.remove(actividad);

    s.getTransaction().commit();
    s.close();
  }

  public List<ActividadRealizada> buscarPorFiltros(int colaboradorId, int solicitudId, LocalDateTime fechaEmision) {
    Session s = HibernateUtil.getSession().openSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<ActividadRealizada> query = cb.createQuery(ActividadRealizada.class);
    Root<ActividadRealizada> root = query.from(ActividadRealizada.class);
    List<Predicate> predicates = new ArrayList<>();

    if (colaboradorId != 0) {
      predicates.add(cb.equal(root.get("colaborador").get("id"), colaboradorId));
    }

    if (solicitudId != 0) {
      predicates.add(cb.equal(root.get("solicitud").get("id"), solicitudId));
    }

    if (fechaEmision != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("fechaEmision"), fechaEmision));
    }

    query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    List<ActividadRealizada> resulta = s.createQuery(query).getResultList();

    s.close();
    return resulta;
  }
  
}
