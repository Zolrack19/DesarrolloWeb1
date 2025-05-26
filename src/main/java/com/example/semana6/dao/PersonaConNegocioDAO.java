package com.example.semana6.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.modelo.PersonaConNegocio;
import com.example.semana6.singleton.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PersonaConNegocioDAO {

  public void crearPersonaConNegocio(PersonaConNegocio PersonaConNegocio) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.persist(PersonaConNegocio);

    s.getTransaction().commit();
    s.close();
  }

  public PersonaConNegocio getById(int id) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    PersonaConNegocio personaConNegocio = s.find(PersonaConNegocio.class, id);

    s.getTransaction().commit();
    s.close();
    return personaConNegocio;
  }

  public PersonaConNegocio getByDocumento(String numDocumento) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    PersonaConNegocio personaConNegocio = s.createQuery("from PersonaConNegocio c where c.numeroDocumento = :doc", PersonaConNegocio.class)
        .setParameter("doc", numDocumento)
        .uniqueResult();

    s.getTransaction().commit();
    s.close();
    return personaConNegocio;
  }

  public PersonaConNegocio getByEmail(String email) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    PersonaConNegocio personaConNegocio = s.createQuery("from PersonaConNegocio c where c.email = :email", PersonaConNegocio.class)
        .setParameter("email", email)
        .uniqueResult();

    s.getTransaction().commit();
    s.close();
    return personaConNegocio;
  }

  public List<PersonaConNegocio> getRango(int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<PersonaConNegocio> personaConNegocios = s.createQuery("from PersonaConNegocio c order by c.id", PersonaConNegocio.class)
        .setFirstResult(inicio)
        .setMaxResults(fin)
        .list();

    s.getTransaction().commit();
    s.close();
    return personaConNegocios;
  }

  public List<PersonaConNegocio> getRangoByTipoDocumento(String tipoDoc, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<PersonaConNegocio> PersonaConNegocios = s.createQuery("from PersonaConNegocio c where c.tipoDocumento = :tipoD order by c.id", PersonaConNegocio.class)
        .setParameter("tipoD", tipoDoc)
        .setFirstResult(inicio)
        .setMaxResults(fin)
        .list();

    s.getTransaction().commit();
    s.close();
    return PersonaConNegocios;
  }

  public List<PersonaConNegocio> getRangoByTipoPersonaConNegocio(String tipoPersonaConNegocio, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<PersonaConNegocio> PersonaConNegocios = s.createQuery("from PersonaConNegocio c where c.tipoPersonaConNegocio = :tipoC order by c.id", PersonaConNegocio.class)
        .setParameter("tipoC", tipoPersonaConNegocio)
        .setFirstResult(inicio)
        .setMaxResults(fin)
        .list();

    s.getTransaction().commit();
    s.close();
    return PersonaConNegocios;
  }

  public List<PersonaConNegocio> getRangoBySectorEconomico(String tipoSectorEcono, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<PersonaConNegocio> PersonaConNegocios = s
        .createQuery("from PersonaConNegocio c where c.sectorEconomico = :tipoSE order by c.id", PersonaConNegocio.class)
        .setParameter("tipoSE", tipoSectorEcono)
        .setFirstResult(inicio)
        .setMaxResults(fin)
        .list();

    s.getTransaction().commit();
    s.close();
    return PersonaConNegocios;
  }

  public List<PersonaConNegocio> getRangoByTelefono(String telefono, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<PersonaConNegocio> PersonaConNegocios = s.createQuery("from PersonaConNegocio c where c.telefono = :telefono order by c.id", PersonaConNegocio.class)
        .setParameter("telefono", telefono)
        .setFirstResult(inicio)
        .setMaxResults(fin)
        .list();

    s.getTransaction().commit();
    s.close();
    return PersonaConNegocios;
  }

  public void actualizarPersonaConNegocio(PersonaConNegocio PersonaConNegocio) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(PersonaConNegocio);

    s.getTransaction().commit();
    s.close();
  }

  public void eliminarPersonaConNegocio(PersonaConNegocio PersonaConNegocio) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(PersonaConNegocio);
    s.remove(PersonaConNegocio);

    s.getTransaction().commit();
    s.close();
  }

  public List<PersonaConNegocio> buscarPorFiltros(short tipoDocId, short tipoPersonaConNegocioId, short sectorEconomicoId,
      String razonSocial, String telefono) {
    Session s = HibernateUtil.getSession().openSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<PersonaConNegocio> query = cb.createQuery(PersonaConNegocio.class);
    Root<PersonaConNegocio> root = query.from(PersonaConNegocio.class);
    List<Predicate> predicates = new ArrayList<>();

    if (tipoDocId != 0) {
      predicates.add(cb.equal(root.get("tipoDocumento").get("id"), tipoDocId));
    }

    if (tipoPersonaConNegocioId != 0) {
      predicates.add(cb.equal(root.get("tipoPersonaConNegocio").get("id"), tipoPersonaConNegocioId));
    }

    if (sectorEconomicoId != 0) {
      predicates.add(cb.equal(root.get("sectorEconomico").get("id"), sectorEconomicoId));
    }

    if (razonSocial != null && !razonSocial.isEmpty()) {
      predicates.add(cb.like(root.get("razonSocial"), razonSocial + "%"));
    }

    if (telefono != null && !telefono.isEmpty()) {
      predicates.add(cb.like(root.get("telefono"), telefono + "%"));
    }

    query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    List<PersonaConNegocio> resulta = s.createQuery(query).getResultList();

    s.close();
    return resulta;
  }

}
