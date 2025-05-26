package com.example.semana6.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.Colaborador;
import com.example.semana6.singleton.HibernateUtil;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ColaboradorDAO {

  public void crearColaborador(Colaborador colaborador) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(colaborador);

    s.getTransaction().commit();
    s.close();
  }

  public Colaborador getById(int id) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Colaborador colaborador = s.find(Colaborador.class, id);

    s.getTransaction().commit();
    s.close();
    return colaborador;
  }

  public Colaborador getByDocumento(String numDocumento) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Colaborador colaborador = s.createQuery("from Colaborador c where c.numeroDocumento = :doc", Colaborador.class)
    .setParameter("doc", numDocumento)
    .uniqueResult();

    s.getTransaction().commit();
    s.close();
    return colaborador;
  }
  
  public Colaborador getByCodigo(String codigo) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Colaborador colaborador = s.createQuery("from Colaborador c where c.codigo = :codigo", Colaborador.class)
    .setParameter("codigo", codigo)
    .uniqueResult();

    s.getTransaction().commit();
    s.close();
    return colaborador;
  }

  public Colaborador getByEmail(String email) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Colaborador colaborador = s.createQuery("from Colaborador c where c.email = :email", Colaborador.class)
    .setParameter("email", email)
    .uniqueResult();

    s.getTransaction().commit();
    s.close();
    return colaborador;
  }

  public List<Colaborador> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Colaborador> colaboradores = s.createQuery("from Colaborador order by id", Colaborador.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    
    s.getTransaction().commit();
    s.close();
    return colaboradores;
  }
  
  public List<Colaborador> getByRolColaborador(short idRolColaborador, int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Colaborador> colaboradores = s.createQuery("from Colaborador c where c.rolColaborador = :idC order by c.id", Colaborador.class)
    .setParameter("idC", idRolColaborador)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    
    s.getTransaction().commit();
    s.close();
    return colaboradores;
  }
  
  public List<Colaborador> getByTipoDocumento(String tipoDoc, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Colaborador> colaboradores = s.createQuery("from Colaborador c where c.tipoDocumento = :tipoD order by c.id", Colaborador.class)
    .setParameter("tipoD", tipoDoc)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return colaboradores;
  }
  
  public List<Colaborador> getByNombre(String nombre, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Colaborador> colaboradores = s.createQuery("from Colaborador c where c.nombre like :nombre order by c.id", Colaborador.class)
    .setParameter("nombre", nombre)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return colaboradores;
  }
  
  public List<Colaborador> getByApellidoPaterno(String apellido, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Colaborador> colaboradores = s.createQuery("from Colaborador c where c.apellidoPaterno like :apellido order by c.id", Colaborador.class)
    .setParameter("apellido", apellido)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return colaboradores;
  }
  
  public List<Colaborador> getByApellidoMaterno(String apellido, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Colaborador> colaboradores = s.createQuery("from Colaborador c where c.apellidoMaterno like :apellido order by c.id", Colaborador.class)
    .setParameter("apellido", apellido)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return colaboradores;
  }

  public void actualizarColaborador(Colaborador colaborador) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(colaborador);

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarColaboradorById(int id) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Query q = s.createQuery("DELETE FROM Colaborador c WHERE c.id = :id")
    .setParameter("id", id);
    q.executeUpdate();

    s.getTransaction().commit();
    s.close();
  }
  
  public void eliminarColaborador(Colaborador colaborador) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(colaborador);
    s.remove(colaborador);

    s.getTransaction().commit();
    s.close();
  }

  public List<Colaborador> buscarPorFiltros(short tipoDocId, short rolColaboradorId, String nombre, String apellidoPaterno) {
    Session s = HibernateUtil.getSession().openSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<Colaborador> query = cb.createQuery(Colaborador.class);
    Root<Colaborador> root = query.from(Colaborador.class);
    List<Predicate> predicates = new ArrayList<>();

    if (tipoDocId != 0) {
      predicates.add(cb.equal(root.get("tipoDocumento").get("id"), tipoDocId));
    }

    if (rolColaboradorId != 0) {
      predicates.add(cb.equal(root.get("rolColaborador").get("id"), rolColaboradorId));
    }

    if (nombre != null && !nombre.isEmpty()) {
      predicates.add(cb.like(root.get("nombre"), nombre + "%"));
    }
    
    if (apellidoPaterno != null && !apellidoPaterno.isEmpty()) {
      predicates.add(cb.like(root.get("apellidoPaterno"), apellidoPaterno + "%"));
    }

    query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    List<Colaborador> resulta = s.createQuery(query).getResultList();

    s.close();
    return resulta;
  }
}
