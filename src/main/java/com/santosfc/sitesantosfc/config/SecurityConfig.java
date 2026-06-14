package com.santosfc.sitesantosfc.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // Spring Security busca: username, password, enabled
        manager.setUsersByUsernameQuery(
            "SELECT email, password, true FROM usuario WHERE email = ?"
        );

        // Spring Security busca: username, authority
        manager.setAuthoritiesByUsernameQuery(
            "SELECT u.email, p.cargo FROM perfil_usuario p " +
            "JOIN usuario u ON p.usuarioid = u.id WHERE u.email = ?"
        );

        return manager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Páginas públicas
                .requestMatchers("/", "/login", "/cadastro", "/cadastroUsuario",
                                "/css/**", "/img/**", "/js/**", "/elenco",
                                "/escudos").permitAll()
                // Somente admin
                .requestMatchers("/listagemUsuario").hasAuthority("admin")
                .requestMatchers("/deletarUsuario/**").hasAuthority("admin")
                // Páginas restritas — qualquer logado
                .requestMatchers("/inscritos/**").authenticated()
                .requestMatchers("/listagemTitulo/**").authenticated()
                .requestMatchers("/jogador/**").authenticated()
                .requestMatchers("/campeonato/**").authenticated()
                .requestMatchers("/inscricao/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/acesso-negado")
        )
            .build();
    }
}