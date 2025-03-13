package dev.yuri.controller;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;  // TextField de JavaFX

public class ClienteController {

    @FXML private TextField txtNome;     // Deve ser TextField do JavaFX
    @FXML private TextField txtCpfCnpj;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtTelefone;

    // Métódo para salvar o cliente
    public void salvarCliente() {
        Cliente cliente = new Cliente(
                txtNome.getText(),
                txtCpfCnpj.getText(),
                txtEndereco.getText(),
                txtTelefone.getText()
        );

        new ClienteDAO().salvarCliente(cliente);
        System.out.println("Cliente salvo com sucesso!");
    }
}
