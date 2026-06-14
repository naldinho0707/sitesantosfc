package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JogadorService {

    @Autowired
    JogadorDAO jogadorDAO;

    public void inserirJogador(Jogador jogador) {
        jogadorDAO.inserirJogador(jogador);
    }

    public ArrayList<Jogador> listarJogadores() {
        return jogadorDAO.listarJogadores();
    }

    public Jogador mostrarJogador(String uuid) {
        return jogadorDAO.mostrarJogador(uuid);
    }

    public void atualizarJogador(Jogador novo, String uuid) {
        jogadorDAO.atualizarJogador(novo, uuid);
    }

    public void deletarJogador(String uuid) {
        jogadorDAO.deletarJogador(uuid);
    }

    public ArrayList<Map<String, Object>> listarInscritos() {
        return jogadorDAO.listarInscritos();
    }
}