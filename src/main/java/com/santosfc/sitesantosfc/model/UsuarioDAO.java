package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class UsuarioDAO {

    @Autowired
    DataSource dataSource;

    @Autowired
    PasswordEncoder passwordEncoder;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    // INSERT — salva usuário com senha criptografada
    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario(nome, email, password) VALUES (?, ?, ?)";
        Object[] obj = new Object[3];
        obj[0] = usuario.getNome();
        obj[1] = usuario.getEmail();
        obj[2] = passwordEncoder.encode(usuario.getPassword());
        jdbc.update(sql, obj);
    }

    // INSERT perfil — usa o UUID direto, cargo fixo "torcedor"
    public void inserirPerfil(String uuid) {
        String sql = "INSERT INTO perfil_usuario(usuarioid, cargo) VALUES (?::uuid, ?)";
        Object[] obj = new Object[2];
        obj[0] = uuid; // usa direto, sem buscar o usuario, pois o UUID já é conhecido
        obj[1] = "torcedor"; // cargo fixo para todos os usuários cadastrados via formulário
        jdbc.update(sql, obj);
    }

    // Busca UUID pelo email
    public String obterUUID(String email) {
        String sql = "SELECT id FROM usuario WHERE email = ?";
        Map<String, Object> mp = jdbc.queryForMap(sql, email);
        UUID uuid = (UUID) mp.get("id");
        return uuid.toString();
    }

    // UPDATE — atualiza nome e email do usuário, mantendo o UUID e a senha inalterados
    public void atualizarUsuario(Usuario novo, String uuid) {
        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE id = ?::uuid";
        Object[] obj = new Object[3];
        obj[0] = novo.getNome();
        obj[1] = novo.getEmail();
        obj[2] = uuid;
        jdbc.update(sql, obj);
    }

    // DELETE — remove usuário pelo UUID
    public void deletarUsuario(String uuid) {
        String sql = "DELETE FROM usuario WHERE id = ?::uuid";
        jdbc.update(sql, uuid);
    }

    // SELECT — busca um usuário pelo UUID
    public Usuario mostrarUsuario(String uuid) {
        String sql = "SELECT * FROM usuario WHERE id = ?::uuid";
        return Usuario.converter(jdbc.queryForMap(sql, uuid));
    }

    /* 
    // SELECT — lista todos os usuários
    public ArrayList<Usuario> listarUsuarios() {
        String sql = "SELECT * FROM usuario";
        return Usuario.converterTodos(jdbc.queryForList(sql));
    }
    */

    // SELECT — lista todos os usuários arrumado para mostrar o cargo
    public ArrayList<Usuario> listarUsuarios() {
    String sql = "SELECT u.id, u.nome, u.email, u.password, p.cargo " +
                 "FROM usuario u " +
                 "LEFT JOIN perfil_usuario p ON p.usuarioid = u.id " +
                 "ORDER BY u.nome ASC";
    return Usuario.converterTodos(jdbc.queryForList(sql));
}

}