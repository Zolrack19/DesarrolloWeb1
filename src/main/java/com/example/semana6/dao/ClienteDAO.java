package com.example.semana6.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import jakarta.persistence.Query;

import com.example.semana6.modelo.Cliente;
import com.example.semana6.singleton.HibernateUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ClienteDAO {

  public void crearCliente(Session s, Cliente cliente) {
    s.persist(cliente);
  }
  
  public Cliente getById(Session s, int id) {
    Cliente cliente = s.find(Cliente.class, id);
    return cliente;
  }
  
  public Cliente getById(int id) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Cliente cliente = s.find(Cliente.class, id);

    s.getTransaction().commit();
    s.close();
    return cliente;
  }
  
  public Cliente getByDocumento(String numDocumento) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    Cliente cliente = s.createQuery("from Cliente c where c.numeroDocumento = :doc", Cliente.class)
    .setParameter("doc", numDocumento)
    .uniqueResult();

    s.getTransaction().commit();
    s.close();
    return cliente;
  }
  
  public Cliente getByEmail(Session s, String email) {
    Cliente cliente = s.createQuery("from Cliente c where c.email = :email", Cliente.class)
    .setParameter("email", email)
    .uniqueResult();
    return cliente;
  }
  
    public List<Cliente> getRango(int inicio, int fin) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Cliente> clientes = s.createQuery("from Cliente order by id desc", Cliente.class)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();
    
    s.getTransaction().commit();
    s.close();
    return clientes;
  }
  
  public List<Cliente> getRangoByTipoDocumento(String tipoDoc, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Cliente> clientes = s.createQuery("from Cliente c where c.tipoDocumento = :tipoD order by c.id", Cliente.class)
    .setParameter("tipoD", tipoDoc)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return clientes;
  }
  
  public List<Cliente> getRangoByTipoCliente(String tipoCliente, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Cliente> clientes = s.createQuery("from Cliente c where c.tipoCliente = :tipoC order by c.id", Cliente.class)
    .setParameter("tipoC", tipoCliente)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return clientes;
  }
  
  public List<Cliente> getRangoBySectorEconomico(String tipoSectorEcono, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Cliente> clientes = s.createQuery("from Cliente c where c.sectorEconomico = :tipoSE order by c.id", Cliente.class)
    .setParameter("tipoSE", tipoSectorEcono)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return clientes;
  }
  
  public List<Cliente> getRangoByTelefono(String telefono, int inicio, int fin) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();

    List<Cliente> clientes = s.createQuery("from Cliente c where c.telefono = :telefono order by c.id", Cliente.class)
    .setParameter("telefono", telefono)
    .setFirstResult(inicio)
    .setMaxResults(fin)
    .list();

    s.getTransaction().commit();
    s.close();
    return clientes;
  }

  public List<Cliente> getClientesByQuery(String query) {
    Session s = HibernateUtil.getSession().openSession();
    
    List<Cliente> clientes = s.createNativeQuery(query, Cliente.class)
    .getResultList();

    s.close();
    return clientes;
  }

  public void actualizarCliente(Cliente cliente) {
    Session s =  HibernateUtil.getSession().openSession();
    s.beginTransaction();

    s.merge(cliente);

    s.getTransaction().commit();
    s.close();
  }

  public void actualizarUnAtributo(Session s, String query, int id, Object valor) {
    Query q = s.createQuery(query)
    .setParameter(1, valor)
    .setParameter(2, id);
    q.executeUpdate();
  }

  public void eliminarClienteById(Session s, int id) {
    Query q = s.createQuery("DELETE FROM Cliente c WHERE c.id = :id")
    .setParameter("id", id);
    q.executeUpdate();
  }
  
  public void eliminarCliente(Session s, Cliente cliente) {
    s.merge(cliente);
    s.remove(cliente);
  }

  public List<Cliente> buscarPorFiltros(short tipoDocId, short tipoClienteId, short sectorEconomicoId,  String razonSocial, String telefono) {
    Session s = HibernateUtil.getSession().openSession();
    CriteriaBuilder cb = s.getCriteriaBuilder();
    CriteriaQuery<Cliente> query = cb.createQuery(Cliente.class);
    Root<Cliente> root = query.from(Cliente.class);
    List<Predicate> predicates = new ArrayList<>();

    if (tipoDocId != 0) {
      predicates.add(cb.equal(root.get("tipoDocumento").get("id"), tipoDocId));
    }

    if (tipoClienteId != 0) {
      predicates.add(cb.equal(root.get("tipoCliente").get("id"), tipoClienteId));
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
    List<Cliente> resulta = s.createQuery(query).getResultList();

    s.close();
    return resulta;
  }

}
