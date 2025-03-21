package dev.yuri.service;

import dev.yuri.DAO.UsuarioDAO;
import dev.yuri.model.Usuario;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario login(String username, String password) {
        return usuarioDAO.autenticar(username, password);
    }

    public void criarUsuario(Usuario usuario) {
        usuarioDAO.salvar(usuario);
    }
}
