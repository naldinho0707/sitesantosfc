package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Jogador {

    private UUID id;
    private String matricula;
    private String nome;
    private int idade;
    private String campeonatoNome;

    public Jogador() {}

    public Jogador(String id, String matricula, String nome, int idade) {
        this.id = UUID.fromString(id);
        this.matricula = matricula;
        this.nome = nome;
        this.idade = idade;
    }

    public Jogador(String matricula, String nome, int idade) {
        this.matricula = matricula;
        this.nome = nome;
        this.idade = idade;
    }

    public void setCampeonatoNome(String campeonatoNome) { 
    this.campeonatoNome = campeonatoNome; 
    }

    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getCampeonatoNome() { return campeonatoNome; }

    
    public static Jogador converter(Map<String, Object> registro) {
        UUID id = (UUID) registro.get("id");
        String matricula = (String) registro.get("matricula");
        String nome = (String) registro.get("nome");
        int idade = (Integer) registro.get("idade");
        Jogador j = new Jogador(id.toString(), matricula, nome, idade);
        // campeonato_nome pode vir nulo se não estiver inscrito
        if (registro.get("campeonato_nome") != null) {
            j.setCampeonatoNome((String) registro.get("campeonato_nome"));
        }
        return j;
    }

    public static ArrayList<Jogador> converterTodos(List<Map<String, Object>> registros) {
        ArrayList<Jogador> aux = new ArrayList<>();
        for (Map<String, Object> registro : registros) {
            aux.add(converter(registro));
        }
        return aux;
    }
}