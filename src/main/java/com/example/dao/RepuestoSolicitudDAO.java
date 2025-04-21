package com.example.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.example.HibernateUtil;
import com.example.dominio.RepuestoSolicitud;

public class RepuestoSolicitudDAO {

  public void crearRepuestoSolicitud(RepuestoSolicitud repuestoSolicitud) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.save(repuestoSolicitud);
    t.commit();
    session.close();
  }

  public RepuestoSolicitud obtenerRepuestoSolicitudPorId(int id) {
    Session session = HibernateUtil.getSession().openSession();
    RepuestoSolicitud RepuestoSolicitud = session.get(RepuestoSolicitud.class, id);
    session.close();
    return RepuestoSolicitud;
  }

  public void actualizarRepuestoSolicitud(int id, RepuestoSolicitud repuestoSolicitudUpdate) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    RepuestoSolicitud repuestoSolicitud = session.get(RepuestoSolicitud.class, id);
    repuestoSolicitud.setEstadoEntrega(repuestoSolicitudUpdate.getEstadoEntrega());
    repuestoSolicitud.setFechaEntrega(repuestoSolicitudUpdate.getFechaEntrega());
    repuestoSolicitud.setFechaSolicitud(repuestoSolicitudUpdate.getFechaSolicitud());
    repuestoSolicitud.setIncidencia(repuestoSolicitudUpdate.getIncidencia());
    repuestoSolicitud.setNombreRepuesto(repuestoSolicitudUpdate.getNombreRepuesto());

    session.update(repuestoSolicitud);
    t.commit();
    session.close();
  }

  public void eliminarRepuestoSolicitud(int id) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();

    session.delete(session.get(RepuestoSolicitud.class, id));

    t.commit();
    session.close();
  }

  public void eliminarRepuestoSolicitud(RepuestoSolicitud repuestoSolicitud) {
    Session session = HibernateUtil.getSession().openSession();
    Transaction t = session.getTransaction();
    session.delete(repuestoSolicitud);
    t.commit();
    session.close();
  }
}
