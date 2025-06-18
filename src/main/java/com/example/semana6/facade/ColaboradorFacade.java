package com.example.semana6.facade;

import java.util.List;

import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dao.RolColaboradorDAO;
import com.example.semana6.dao.TipoDocumentoDAO;
import com.example.semana6.dto.colaborador.ColaboradorCrear;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Colaborador;

public class ColaboradorFacade {
  private ColaboradorDAO colaboradorDAO = new ColaboradorDAO();
  private TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
  private RolColaboradorDAO rolColaboradorDAO = new RolColaboradorDAO();
  private StringBuilder query = new StringBuilder("""
    SELECT c.*
    FROM colaboradordto_vista c WHERE 
  """);
  
  public ColaboradorVista crearColaborador(ColaboradorCrear colaboradorcCrear) {
    Colaborador colaborador = colaboradorcCrear.toColaborador();
    colaborador.setTipoDocumento(tipoDocumentoDAO.getById(colaboradorcCrear.getTipoDocumentoId()));
    colaborador.setRolColaborador(rolColaboradorDAO.getById(colaboradorcCrear.getRolColaboradorId()));
    colaboradorDAO.crearColaborador(colaborador);
    ColaboradorVista colaboradorVista = new ColaboradorVista(colaborador);
    return colaboradorVista;
  }

    public List<ColaboradorVista> getColaboradores(String[] tokens) {
    if (tokens.length == 0) return null;
    query.delete(49, query.length());

    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i];
      if (i != 0) {
        query.append(" OR ");
      }
      query.append("(\n");
      query.append("c.nombre ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.apellido_paterno ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.apellido_materno ILIKE unaccent('%").append(token); 
      query.append("%') OR \n");
      query.append("c.numero_documento LIKE ('").append(token); 
      query.append("%')");
      query.append(")\n");
    }
    query.append("limit 5");
    List<ColaboradorVista> colaboradores = colaboradorDAO.getClientesByQuery(query.toString());
    return colaboradores;
  }

  public List<ColaboradorVista> getColaboradores(int numPag) {
    List<ColaboradorVista> colaboradores = colaboradorDAO.getRangoVista((numPag - 1)*10, 10);
    if (colaboradores.size() == 0) return null;
    return colaboradores;
  }

  public List<ColaboradorVista> getColaboradores(int solicitudId, int usuarioId) {
    return null;
  }
}
