package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class JogadorDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void inserirJogador(Jogador jogador) {
        String sql = "INSERT INTO jogador(matricula, nome, idade) VALUES (?, ?, ?)";
        jdbc.update(sql, jogador.getMatricula(), jogador.getNome(), jogador.getIdade());
    }

    // ✅ ATUALIZADO — traz o campeonato junto via LEFT JOIN
    public ArrayList<Jogador> listarJogadores() {
        String sql = "SELECT j.id, j.matricula, j.nome, j.idade, " +
                     "c.nome AS campeonato_nome " +
                     "FROM jogador j " +
                     "LEFT JOIN inscricao i ON j.id = i.jogador_id " +
                     "LEFT JOIN campeonato c ON i.campeonato_id = c.id " +
                     "ORDER BY j.nome ASC";
        return Jogador.converterTodos(jdbc.queryForList(sql));
    }

    public Jogador mostrarJogador(String uuid) {
        String sql = "SELECT * FROM jogador WHERE id = ?::uuid";
        return Jogador.converter(jdbc.queryForMap(sql, uuid));
    }

    public void atualizarJogador(Jogador novo, String uuid) {
        String sql = "UPDATE jogador SET matricula = ?, nome = ?, idade = ? WHERE id = ?::uuid";
        jdbc.update(sql, novo.getMatricula(), novo.getNome(), novo.getIdade(), uuid);
    }

    public void deletarJogador(String uuid) {
        String sql = "DELETE FROM jogador WHERE id = ?::uuid";
        jdbc.update(sql, uuid);
    }

    // Listar campeonatos em que o jogador está inscrito
    public ArrayList<Campeonato> listarInscricoes(String uuidJogador) {
        String sql = "SELECT campeonato.* FROM campeonato " +
                     "INNER JOIN inscricao ON campeonato.id = inscricao.campeonato_id " +
                     "WHERE inscricao.jogador_id = ?::uuid";
        return Campeonato.converterTodos(jdbc.queryForList(sql, uuidJogador));
    }

    // ✅ NOVO — lista todos os inscritos com dados do jogador e campeonato
    public ArrayList<Map<String, Object>> listarInscritos() {
        String sql = "SELECT j.matricula, j.nome AS jogador_nome, " +
                     "c.nome AS campeonato_nome, i.data_inscricao " +
                     "FROM inscricao i " +
                     "JOIN jogador j ON i.jogador_id = j.id " +
                     "JOIN campeonato c ON i.campeonato_id = c.id " +
                     "ORDER BY i.data_inscricao DESC";
        return (ArrayList<Map<String, Object>>) jdbc.queryForList(sql);
    }
}