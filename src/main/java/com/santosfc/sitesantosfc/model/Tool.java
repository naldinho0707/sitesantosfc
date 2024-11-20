package com.santosfc.sitesantosfc.model;

import java.util.Map;

// converter o Map<String,Object>  em um objeto da classe Titulo
public class Tool {

    // recebe um Map e retornar um titulo (Titulo no formato objeto do java)
    //  por causa do thymeleaf.org  porque o MAP retorna "JSON" e não objeto java
    public static Titulo converterTitulo(Map<String,Object> registro){
        // usar downcast para os objetos do Map (Interger = inteiro)
        // Como registro.get retorna um Object, devemos usar o polimorfismo
        // se subtipos ("String", "Interger") (downcast) para recuperar os tipos originais
        // os tipos devem ser os mesmos do banco de dados, exemplo varchar no banco e aqui inteiro
        // vai dar erro
        return new Titulo((Integer) registro.get("id")
                        ,(String) registro.get("qt_titulos")
                        ,(String) registro.get("competicao")
                        ,(String) registro.get("temporadas")
                        ,(String) registro.get("imagem"));
    }


}
