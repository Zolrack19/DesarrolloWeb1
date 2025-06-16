package com.example.semana6.facade;

import java.util.ArrayList;
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

  
  public ColaboradorVista crearColaborador(ColaboradorCrear colaboradorcCrear) {
    Colaborador colaborador = colaboradorcCrear.toColaborador();
    colaborador.setTipoDocumento(tipoDocumentoDAO.getById(colaboradorcCrear.getTipoDocumentoId()));
    colaborador.setRolColaborador(rolColaboradorDAO.getById(colaboradorcCrear.getRolColaboradorId()));
    colaboradorDAO.crearColaborador(colaborador);
    ColaboradorVista colaboradorVista = new ColaboradorVista(colaborador);
    System.out.println("todo bien!!");
    return colaboradorVista;
  }


  public List<ColaboradorVista> getcolaboradores(int numPag) {
    List<Colaborador> colaboradores = colaboradorDAO.getRango((numPag - 1)*10, 10);
    if (colaboradores.size() == 0) return null;
    List<ColaboradorVista> colaboradoresVista = new ArrayList<>();
    colaboradores.forEach((colaborador) -> {
      colaboradoresVista.add(new ColaboradorVista(colaborador));
    });
    return colaboradoresVista;
  }
}
