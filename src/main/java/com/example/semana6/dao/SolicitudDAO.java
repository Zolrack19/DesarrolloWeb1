package com.example.semana6.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.dto.solicitud.SolicitudVista;
import com.example.semana6.modelo.Solicitud;
import com.example.semana6.singleton.HibernateUtil;
import com.example.semana6.singleton.ValorDefecto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SolicitudDAO {
  
  public void crearSolicitud(Solicitud solicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(solicitud);
    s.flush();
    s.refresh(solicitud);

    s.getTransaction().commit();
    s.close();
  }

  public Solicitud getById(Session s, int id) {
    Solicitud solicitud = s.find(Solicitud.class, id);
    return solicitud;
  }

  public Solicitud getById(int id) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Solicitud solicitud = s.find(Solicitud.class, id);

    s.getTransaction().commit();
    s.close();
    return solicitud;
  }

  public List<Solicitud> getRangoDescendiente(int idLimite, int maxResultados, boolean paginaSiguiente) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    List<Solicitud> solicitudes = null;

    if (idLimite == ValorDefecto.VALOR_NULO.getValue()) {
      solicitudes = s.createQuery("from Solicitud s order by s.id desc", Solicitud.class)
      .setMaxResults(maxResultados)
      .list();
  
    } else if (paginaSiguiente) {
      solicitudes = s.createQuery("from Solicitud s where s.id < :idLimite order by s.id desc", Solicitud.class)
      .setParameter("idLimite", idLimite)
      .setMaxResults(maxResultados)
      .list();
  
    } else {
      solicitudes = s.createQuery("from Solicitud s where s.id > :idLimite order by s.id", Solicitud.class)
      .setParameter("idLimite", idLimite)
      .setMaxResults(maxResultados)
      .list();

      Collections.reverse(solicitudes);
    }

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }

  @SuppressWarnings("unchecked")
  public List<SolicitudVista> getRangoVista(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<SolicitudVista> solicitudes = s.createNativeQuery("select * from solicituddto_vista s order by s.id desc", "SolicitudVistaMapping")
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }

  @SuppressWarnings("unchecked")
  public List<SolicitudVista> getByClienteId(int clienteId, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<SolicitudVista> solicitudes = s.createNativeQuery("select * from solicituddto_vista s where s.cliente_id = :id order by s.id desc", "SolicitudVistaMapping")
    .setParameter("id", clienteId)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }

  
  public List<Solicitud> getByTipoSolicitud(int tipoSolicitudId, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.tipoSolicitud.id = :tipoS order by s.id", Solicitud.class)
    .setParameter("tipoS", tipoSolicitudId)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }
  
  public List<Solicitud> getByCordinadorId(int colaboradorId, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.cordinador.id = :idC order by s.id", Solicitud.class)
    .setParameter("idC", colaboradorId)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }

  
  public List<Solicitud> getByEstadoSolicitudId(short estadoSolicitudId, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.estadoSolicitud.id = :idES order by s.id", Solicitud.class)
    .setParameter("idES", estadoSolicitudId)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }
  
  // TODO: = > <
  public List<Solicitud> getByFechaRegistro(LocalDateTime fecha, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.fechaRegistro = :fecha order by s.id", Solicitud.class)
    .setParameter("fecha", fecha)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }
  
  public List<Solicitud> getByFechaRegistro(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.fechaRegistro between :fechaI and :fechaF order by s.id", Solicitud.class)
    .setParameter("fechaI", fechaInicio)
    .setParameter("fechaF", fechaFin)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }

  // TODO: = > <
  public List<Solicitud> getByFechaFinalizacion(LocalDateTime fecha, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.fechaFinalizacion = :fecha order by s.id", Solicitud.class)
    .setParameter("fecha", fecha)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }
  
  public List<Solicitud> getByFechaFinalizacion(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Solicitud> solicitudes = s.createQuery("from Solicitud s where s.fechaFinalizacion between :fechaI and :fechaF order by s.id", Solicitud.class)
    .setParameter("fechaI", fechaInicio)
    .setParameter("fechaF", fechaFin)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return solicitudes;
  }

  
  public void actualizarSolicitud(Solicitud solicitud) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();
  
    s.merge(solicitud);
  
    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarSolicitud(Session s, Solicitud solicitud) {
    s.merge(solicitud);
    s.remove(solicitud);
  }

  public List<Solicitud> buscarPorFiltros(short tipoSolicitdId, short estadoSolicitudId, int colaboradorId, int clienteId, LocalDateTime fechaRegistro, LocalDateTime fechaFinalizacion) {
    Session s = HibernateUtil.getSession().openSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<Solicitud> query = cb.createQuery(Solicitud.class);
    Root<Solicitud> root = query.from(Solicitud.class);
    List<Predicate> predicates = new ArrayList<>();

    if (tipoSolicitdId != 0) {
      predicates.add(cb.equal(root.get("tipoSolicitud").get("id"), tipoSolicitdId));
    }

    if (estadoSolicitudId != 0) {
      predicates.add(cb.equal(root.get("estadoSolicitud").get("id"), estadoSolicitudId));
    }

    if (colaboradorId != 0) {
      predicates.add(cb.equal(root.get("cordinador").get("id"), colaboradorId));
    }
    
    if (clienteId != 0) {
      predicates.add(cb.equal(root.get("cliente").get("id"), clienteId));
    }

    if (fechaRegistro != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("fechaRegistro"), fechaRegistro));
    }
    
    if (fechaFinalizacion != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("fechaFinalizacion"), fechaFinalizacion));
    }

    query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    List<Solicitud> resulta = s.createQuery(query).getResultList();

    s.close();
    return resulta;
  }
}
