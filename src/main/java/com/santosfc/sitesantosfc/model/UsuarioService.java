package com.santosfc.sitesantosfc.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDAO udao;

    public void inserirUsuario(Usuario usuario) {
        udao.inserirUsuario(usuario);
    }

    public String obterUUID(String email) {
        return udao.obterUUID(email);
    }

    public void inserirPerfil(String uuid) {
        udao.inserirPerfil(uuid);
    }

    public ArrayList<Usuario> listarUsuarios() {
        return udao.listarUsuarios();
    }

    public Usuario mostrarUsuario(String uuid) {
        return udao.mostrarUsuario(uuid);
    }

    public void atualizarUsuario(Usuario novo, String uuid) {
        udao.atualizarUsuario(novo, uuid);
    }

    public void deletarUsuario(String uuid) {
        udao.deletarUsuario(uuid);
    }
}
