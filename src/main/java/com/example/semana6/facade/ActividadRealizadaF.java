package com.example.semana6.facade;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import com.example.semana6.dao.ActividadRealizadaDAO;
import com.example.semana6.dao.AsignacionDAO;
import com.example.semana6.dto.actividadRealizada.ActividadRealizadaCrear;
import com.example.semana6.dto.actividadRealizada.ActividadRealizadaVista;
import com.example.semana6.dto.cliente.ClienteVista;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.ActividadRealizada;
import com.example.semana6.modelo.Asignacion;
import com.example.semana6.modelo.AsignacionId;
import com.example.semana6.singleton.HibernateUtil;

public class ActividadRealizadaF {

  private ActividadRealizadaDAO actividadRealizadaDAO = new ActividadRealizadaDAO();
  private AsignacionDAO asignacionDAO = new AsignacionDAO();

  public ActividadRealizadaVista crearActividad(ActividadRealizadaCrear actividadRealizadaCrear) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {

      ActividadRealizada actividadRealizada = actividadRealizadaCrear.toActividadRealizada();
      Asignacion asignacion = asignacionDAO.getById(s, new AsignacionId(actividadRealizadaCrear.getSolicitudId(), actividadRealizadaCrear.getColaboradorId()));
      
      if (asignacion == null) {
        return new ActividadRealizadaVista(String.format("Error. Asignación de colaborador y solicitud inválida.\nSolicitud id: %d\nColaborador id: %d",
        actividadRealizadaCrear.getSolicitudId(),
        actividadRealizadaCrear.getColaboradorId()));
      }
      actividadRealizada.setColaborador(asignacion.getColaborador());
      actividadRealizada.setSolicitud(asignacion.getSolicitud());

      actividadRealizadaDAO.crearActividad(s, actividadRealizada);
      ActividadRealizadaVista actividadRealizadaVista = new ActividadRealizadaVista(actividadRealizada);
      s.getTransaction().commit();
      return actividadRealizadaVista;
    } catch (Exception e) {
      s.getTransaction().rollback();
      e.printStackTrace();
    
    } finally {
      s.close();
    }

    return null;
  }

  public List<ActividadRealizadaVista> getActividadesByAsignacion(int colaboradorId, int solicitudId, Object usuario) {
    Session s = HibernateUtil.getSession().openSession();
    s.beginTransaction();
    try {
      if (usuario instanceof ClienteVista clienteVista) { // Solicitud no pertenece al cliente
        Asignacion asignacion = asignacionDAO.getById(s, new AsignacionId(solicitudId, colaboradorId));
        if (asignacion == null || asignacion.getSolicitud().getCliente().getId() != clienteVista.getId()) return null;
        
      } else if (usuario instanceof ColaboradorVista colaboradorVista) { // Colaborador no pertenece en la solicitud
        if (!colaboradorVista.getRolColaborador().equals("Administrador")) {
          if (asignacionDAO.getById(s, new AsignacionId(solicitudId, colaboradorVista.getId())) == null) return null;
        }
      }

      List<ActividadRealizada> actividades = actividadRealizadaDAO.getByIdColaboradorSolicitud(s, colaboradorId, solicitudId);
      if (actividades == null ) return null;
      
      List<ActividadRealizadaVista> actividadesVista = new LinkedList<>();
      for (ActividadRealizada actividad : actividades) {
        actividadesVista.add(new ActividadRealizadaVista(actividad));
      }

      return actividadesVista;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
