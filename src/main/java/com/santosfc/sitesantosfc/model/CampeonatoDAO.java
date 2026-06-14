package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class CampeonatoDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void inserirCampeonato(Campeonato campeonato) {
        String sql = "INSERT INTO campeonato(nome, temporada) VALUES (?, ?)";
        jdbc.update(sql, campeonato.getNome(), campeonato.getTemporada());
    }

    public ArrayList<Campeonato> listarCampeonatos() {
        String sql = "SELECT * FROM campeonato ORDER BY temporada DESC";
        return Campeonato.converterTodos(jdbc.queryForList(sql));
    }

    public Campeonato mostrarCampeonato(String uuid) {
        String sql = "SELECT * FROM campeonato WHERE id = ?::uuid";
        return Campeonato.converter(jdbc.queryForMap(sql, uuid));
    }

    public void deletarCampeonato(String uuid) {
        String sql = "DELETE FROM campeonato WHERE id = ?::uuid";
        jdbc.update(sql, uuid);
    }

    // Inscrever jogador em campeonato
    public void inscrever(String jogadorId, String campeonatoId) {
        String sql = "INSERT INTO inscricao(jogador_id, campeonato_id) VALUES (?::uuid, ?::uuid)";
        jdbc.update(sql, jogadorId, campeonatoId);
    }
}