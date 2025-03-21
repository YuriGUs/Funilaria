package dev.yuri.controller;

import dev.yuri.App;
import dev.yuri.DAO.UsuarioDAO;
import dev.yuri.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField txtUsername;    // Campo de texto para o username
    @FXML private PasswordField txtPassword; // Campo de senha
    @FXML private Label lblErro;           // Label para mensagens de erro

    private UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instância do DAO para autenticação

    @FXML
    private void login() {
        String username = txtUsername.getText();   // Pega o texto do campo username
        String password = txtPassword.getText();   // Pega o texto do campo senha

        if (username.isEmpty() || password.isEmpty()) {
            lblErro.setText("Preencha todos os campos!");
            return;
        }

        Usuario usuario = usuarioDAO.autenticar(username, password);

        if (usuario != null) {
            try {
                App.carregarTelaPrincipal();
            } catch (Exception e) {
                lblErro.setText("Erro ao carregar a tela principal: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            lblErro.setText("Usuário ou senha inválidos!");
        }
    }
}