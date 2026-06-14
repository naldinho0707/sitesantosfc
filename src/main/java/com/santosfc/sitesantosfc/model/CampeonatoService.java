package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampeonatoService {

    @Autowired
    CampeonatoDAO campeonatoDAO;

    public void inserirCampeonato(Campeonato campeonato) {
        campeonatoDAO.inserirCampeonato(campeonato);
    }

    public ArrayList<Campeonato> listarCampeonatos() {
        return campeonatoDAO.listarCampeonatos();
    }

    public Campeonato mostrarCampeonato(String uuid) {
        return campeonatoDAO.mostrarCampeonato(uuid);
    }

    public void deletarCampeonato(String uuid) {
        campeonatoDAO.deletarCampeonato(uuid);
    }

    public void inscrever(String jogadorId, String campeonatoId) {
        campeonatoDAO.inscrever(jogadorId, campeonatoId);
    }
}