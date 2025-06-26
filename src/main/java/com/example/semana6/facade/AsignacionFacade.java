package com.example.semana6.facade;

import org.hibernate.Session;

import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dto.cliente.ClienteDTO;
import com.example.semana6.dto.colaborador.ColaboradorDTO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.singleton.HibernateUtil;

public class AsignacionFacade {
  private AsignacionDAO asignacionDAO = new AsignacionDAO();

  public void eliminarAsignacion(Object usuario, int colaboradorId, int solicitudId) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    Asignacion asignacion = asignacionDAO.getById(s, new AsignacionId(solicitudId, colaboradorId));
    if (asignacion == null) {
      System.out.println("no hay asignación a eliminar");
      s.close();
      return;
    }
    if (usuario instanceof ClienteDTO) {
      s.close();
      return;
    } else if (usuario instanceof ColaboradorDTO) {
      if (!((ColaboradorVista) usuario).getRolColaborador().equals("Administrador")) {
        s.close();
        return;
      }
    }
    asignacionDAO.eliminarAsignacion(s, asignacion);
    
    s.getTransaction().commit();
    s.close();
  }
}
