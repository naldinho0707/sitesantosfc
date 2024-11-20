package com.santosfc.sitesantosfc.model;

public class Titulo {

    private int id;
    private String qt_titulos, competicao, temporadas, imagem;


    // construtor vazio para criar a tabela
    public Titulo(){
    }

    // porque 02 contrutores 01 para o banco de dados sem id porque o banco cria i Id (autoincremento)
    // sobrecarga dos contrutores Cliente
    // este construtor para poder alterar os dados
    public Titulo(int id, String qt_titulos, String competicao, String temporadas, String imagem) {
        this.id = id;
        this.qt_titulos = qt_titulos;
        this.competicao = competicao;
        this.temporadas = temporadas;
        this.imagem = imagem;
    }
    
    // porque 02 contrutores este para o banco de dados sem id porque o banco cria o Id (autoincremento)
    //  não preciso do id para cadastrar, por isso uso da sobrecarga
    public Titulo(String qt_titulos, String competicao, String temporadas, String imagem) {
        this.qt_titulos = qt_titulos;
        this.competicao = competicao;
        this.temporadas = temporadas;
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQt_titulos() {
        return qt_titulos;
    }

    public void setQt_titulos(String qt_titulos) {
        this.qt_titulos = qt_titulos;
    }

    public String getCompeticao() {
        return competicao;
    }

    public void setCompeticao(String competicao) {
        this.competicao = competicao;
    }

    public String getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(String temporadas) {
        this.temporadas = temporadas;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

}
