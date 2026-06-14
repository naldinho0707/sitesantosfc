package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Usuario {

    private UUID id;
    private String nome;
    private String email;
    private String password;
    private String cargo;

    //Constrtuor para a pagina do formulario
    public Usuario() {}


    //Construtor bom para Select
    public Usuario(String id, String nome, String email, String password) {
        this.id = UUID.fromString(id);
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    //Construtor bom para insert
    // password NÃO está aqui pois vem pelo setPassword() via formulário Thymeleaf
    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    // Id é do tipo UUID, então os métodos getId e setId precisam lidar com UUID
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    //Nome, email e password são do tipo String, então os métodos get e set para esses campos permanecem inalterados
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    //Email
    public String getEmail() {
        return email; 
    }
    public void setEmail(String email) {
        this.email = email; 
    }

    //Password
    public String getPassword() {
        return password; 
    }
    public void setPassword(String password) {
        this.password = password; 
    }

    //mostrar o cargo na listagem do usuário
    public String getCargo() {
        return cargo; 
        }
    public void setCargo(String cargo) {
        this.cargo = cargo; 
        }


    // Método para converter um registro do banco de dados (Map<String, Object>) em um objeto Usuario
    public static Usuario converter(Map<String, Object> registro) {
        UUID id = (UUID) registro.get("id");
        String nome = (String) registro.get("nome");
        String email = (String) registro.get("email");
        String password = (String) registro.get("password");
        String cargo = registro.get("cargo") != null ? (String) registro.get("cargo") : "sem cargo";
        Usuario u = new Usuario(id.toString(), nome, email, password);
        u.setCargo(cargo);
        return u;
    }

    /*
    // Método para converter um registro do banco de dados (Map<String, Object>) em um objeto Usuario
    public static Usuario converter(Map<String,Object> registro){
        String nome = (String) registro.get("nome");
        UUID id = (UUID) registro.get("id");
        String email = (String) registro.get("email");
        String password = (String) registro.get("password");
        return new Usuario(id.toString(), nome, email, password);
    }
    */

    public static ArrayList<Usuario> converterTodos(List<Map<String,Object>> registros){
        ArrayList<Usuario> aux = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            aux.add(converter(registro));
        }
        return aux;
    }   





}