package com.santosfc.sitesantosfc.model;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class TituloDAO {
// DAO = Data Acssess Object

@Autowired
DataSource dataSource;

JdbcTemplate jdbc;

@PostConstruct
private void initialize(){
    jdbc = new JdbcTemplate(dataSource);
}

public void inserirTitulo(Titulo titulo){
    String sql = "INSERT INTO titulos(competicao, qt_titulos, temporadas, imagem) VALUES (?,?,?,?);";
    Object[] parametros = new Object[4];
    parametros[0] = titulo.getCompeticao();
    parametros[1] = titulo.getQt_titulos();
    parametros[2] = titulo.getTemporadas();
    parametros[3] = titulo.getImagem();
    jdbc.update(sql, parametros);
}

// Obs.: Cada método criado tem que fazer um serviço no TituloService.java
// Método para retornar uma lista dos Títulos , "Tipo uma lista de JSON"
//retorno lista [Map]=> [{id: 1, competicao: barsileiro, qt_titulos: 8,
//  temporadas: 2014, 2022 ..., imagem: taca_brasileiro.jpg} ]
public List<Map<String, Object>> obterTodosTitulos(){
    String sql = "Select * from titulos ORDER BY id ASC;";
    return jdbc.queryForList(sql);
}

// Método para atualizar o título
public void atualizarTitulo(int id, Titulo titulo){
    String sql = "UPDATE titulos SET ";
    sql += "competicao = ?, qt_titulos = ?, temporadas = ?, imagem = ? WHERE id = ?";
    jdbc.update(sql, titulo.getCompeticao(), titulo.getQt_titulos(), titulo.getTemporadas(),
    titulo.getImagem(), id);
}

// Método obter 01 título específico a partir da informação de seu respectivo id
public Titulo obterTitulo(int id){
    String sql = "Select * from titulos where id = ?";
    // converter JSON para titulo, passando o id
    // queryForMap pega 01 registro o queryForList pega todos
    return Tool.converterTitulo(jdbc.queryForMap(sql, id));
}

// Método deletar título
public void deletarTitulo(int id){
   String sql = "DELETE FROM titulos WHERE id = ?";
   jdbc.update(sql, id);
}
}
