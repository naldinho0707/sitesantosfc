package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Campeonato {

    private UUID id;
    private String nome;
    private String temporada;

    public Campeonato() {}

    public Campeonato(String id, String nome, String temporada) {
        this.id = UUID.fromString(id);
        this.nome = nome;
        this.temporada = temporada;
    }

    public Campeonato(String nome, String temporada) {
        this.nome = nome;
        this.temporada = temporada;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTemporada() { return temporada; }
    public void setTemporada(String temporada) { this.temporada = temporada; }

    public static Campeonato converter(Map<String, Object> registro) {
        UUID id = (UUID) registro.get("id");
        String nome = (String) registro.get("nome");
        String temporada = (String) registro.get("temporada");
        return new Campeonato(id.toString(), nome, temporada);
    }

    public static ArrayList<Campeonato> converterTodos(List<Map<String, Object>> registros) {
        ArrayList<Campeonato> aux = new ArrayList<>();
        for (Map<String, Object> registro : registros) {
            aux.add(converter(registro));
        }
        return aux;
    }
}