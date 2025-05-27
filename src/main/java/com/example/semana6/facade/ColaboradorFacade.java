package com.example.semana6.facade;

import java.util.ArrayList;
import java.util.List;

import com.example.semana6.dao.ColaboradorDAO;
import com.example.semana6.dto.colaborador.ColaboradorVista;
import com.example.semana6.modelo.Colaborador;

public class ColaboradorFacade {
  private ColaboradorDAO colaboradorDAO = new ColaboradorDAO();

  
  public List<ColaboradorVista> getcolaboradores(int inicio, int fin) {
    List<Colaborador> colaboradores = colaboradorDAO.getRango(inicio, fin);
    if (colaboradores.size() == 0) return null;
    List<ColaboradorVista> colaboradoresVista = new ArrayList<>();
    colaboradores.forEach((colaborador) -> {
      colaboradoresVista.add(new ColaboradorVista(colaborador));
    });
    return colaboradoresVista;
  }
}
