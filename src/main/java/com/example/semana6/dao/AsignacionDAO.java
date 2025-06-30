package com.example.semana6.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.singleton.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class AsignacionDAO {

  public void crearAsignacion(Session s, Asignacion asignacion) {
    s.persist(asignacion);
  }

  public void crearAsignacion(Asignacion asignacion) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(asignacion);

    s.getTransaction().commit();
    s.close();
  }

  public Asignacion getById(Session s, AsignacionId id) {
    Asignacion asignacion = s.find(Asignacion.class, id);
    return asignacion;
  }
  
  public List<Asignacion> getRango(int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();

    List<Asignacion> asignaciones = s.createQuery("from Asignacion a order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return asignaciones;
  }
  
  public List<Asignacion> getByIdColaborador(Session s, int colaboradorId, int inicio, int fin) {
    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.colaborador.id = :id order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("id", colaboradorId)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    return asignaciones;
  }
  
  public List<Asignacion> getByIdSolicitud(int solicitudId) {
    Session s = HibernateUtil.getSession().openSession();

    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.solicitud.id = :id order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("id", solicitudId)
    .list();

    s.close();
    return asignaciones;
  }

  //TODO: esto cambiar por un enum para decidir si >=, <= o =
  public List<Asignacion> getRangoByFechaInicioAtencion(LocalDateTime fecha, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.inicioAtencion = :inicio order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("inicio", fecha)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return asignaciones;
  }
  
  public List<Asignacion> getRangoByFechaInicioAtencion(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.inicioAtencion between :inicio and :fin order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("inicio", fechaInicio)
    .setParameter("fin", fechaFin)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return asignaciones;
  }

  //TODO: esto cambiar por un enum para decidir si >=, <= o =
  public List<Asignacion> getRangoByFechaFinAtencion(LocalDateTime fecha, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.finAtencion = :fin order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("fin", fecha)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return asignaciones;
  }
  
  public List<Asignacion> getRangoByFechaFinAtencion(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.finAtencion between :inicio and :fin order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("inicio", fechaInicio)
    .setParameter("fin", fechaFin)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return asignaciones;
  }

  public List<Asignacion> getRangoByFechaAtencion(LocalDateTime fechaInicioAtencion, LocalDateTime fechaFinAtencion, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    
    List<Asignacion> asignaciones = s.createQuery("from Asignacion a where a.inicioAtencion >= :inicio and a.finAtencion <= :fin order by a.id.solicitudId, a.id.colaboradorId", Asignacion.class)
    .setParameter("inicio", fechaInicioAtencion)
    .setParameter("fin", fechaFinAtencion)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.close();
    return asignaciones;
  }

  public void actualizarAsignacion(Asignacion asignacion) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(asignacion);

    s.getTransaction().commit();
    s.close();
  }

  public void eliminarAsignacion(Session s, Asignacion asignacion) {
    s.merge(asignacion);
    s.remove(asignacion);
  }

  public List<Asignacion> buscarPorFiltros(int colaboradorId, int solicitudId, LocalDateTime fechaInicioAtencion, LocalDateTime fechaFinAtencion) {
    Session s = HibernateUtil.getSession().openSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<Asignacion> query = cb.createQuery(Asignacion.class);
    Root<Asignacion> root = query.from(Asignacion.class);
    List<Predicate> predicates = new ArrayList<>();

    if (colaboradorId != 0) {
      predicates.add(cb.equal(root.get("colaborador").get("id"), colaboradorId));
    }

    if (solicitudId != 0) {
      predicates.add(cb.equal(root.get("solicitud").get("id"), solicitudId));
    }

    if (fechaInicioAtencion != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("inicioAtencion"), fechaInicioAtencion));
    }
    
    if (fechaFinAtencion != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("finAtencion"), fechaFinAtencion));
    }

    query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    List<Asignacion> resulta = s.createQuery(query).getResultList();

    s.close();
    return resulta;
  }

}
