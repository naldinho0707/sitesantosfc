package com.santosfc.sitesantosfc.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TituloService {

    @Autowired
    TituloDAO tdao;

    public void inserirTitulo(Titulo titulo){
        tdao.inserirTitulo(titulo);
    }

    // Serviço do método, pode ser mesmo nome, se tiver return
    // o serviço tem que ter return também, depois criar template
    public List<Map<String, Object>> obterTodosTitulos(){
        return tdao.obterTodosTitulos();
    }

    public void atualizarTitulo(int id, Titulo titulo){
        tdao.atualizarTitulo(id, titulo);
    }

    public Titulo obterTitulo(int id){
        return tdao.obterTitulo(id);
    }

    public void deletarTitulo(int id){
        tdao.deletarTitulo(id);
    }

}
