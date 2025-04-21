package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.Persona;

public class PersonaDAO {

  public void crearPersona(Persona persona) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(persona);
    t.commit();
    session.close();
  }

  public Persona obtenerPersonaPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Persona persona = session.get(Persona.class, id);
    session.close();
    return persona;
  }

  public void actualizarPersona(int id, Persona personaUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    Persona persona = session.get(Persona.class, id);
    persona.setApellido(personaUpdate.getApellido());
    persona.setContrasena(personaUpdate.getContrasena());
    persona.setCorreo(personaUpdate.getCorreo());
    persona.setNombre(personaUpdate.getNombre());
    persona.setNumeroCeluar(personaUpdate.getNumeroCeluar());

    session.update(persona);
    t.commit();
    session.close();
  }

  public void eliminarPersona(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    session.delete(session.get(Persona.class, id));

    t.commit();
    session.close();
  }

  public void eliminarPersona(Persona persona) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(persona);
    t.commit();
    session.close();
  }
}
