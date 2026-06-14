package com.santosfc.sitesantosfc.config;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {

    @Bean
    public CommandLineRunner seedAdmin(DataSource dataSource, PasswordEncoder encoder) {
        return args -> {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);

            Integer existing = jdbc.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE email = ?",
                Integer.class, "admin@santos.com");

            if (existing != null && existing == 0) {
                jdbc.update(
                    "INSERT INTO usuario(nome, email, password) VALUES (?, ?, ?)",
                    "Admin", "admin@santos.com", encoder.encode("admin123"));
            }

            jdbc.update(
                "INSERT INTO perfil_usuario(usuarioid, cargo) " +
                "SELECT id, 'admin' FROM usuario WHERE email = 'admin@santos.com' " +
                "ON CONFLICT (usuarioid) DO UPDATE SET cargo = 'admin'");
        };
    }
}
