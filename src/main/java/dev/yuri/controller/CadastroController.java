package dev.yuri.controller;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.DAO.VeiculoDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CadastroController {

    @FXML
    private TextField txtNome, txtCpfCnpj, txtEndereco, txtTelefone;
    @FXML
    private TextField txtPlaca, txtModelo, txtAno, txtCor;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private Cliente clienteAtual;

    @FXML
    private void cadastrarCliente() {
        clienteAtual = new Cliente(0, txtNome.getText(), txtCpfCnpj.getText(), txtEndereco.getText(), txtTelefone.getText());
        clienteDAO.inserirCliente(clienteAtual);
    }

    @FXML
    private void cadastrarVeiculo() {
        if (clienteAtual == null) {
            System.out.println("Cadastre um cliente primeiro!");
            return;
        }
        Veiculo veiculo = new Veiculo(0, clienteAtual.getId(), txtPlaca.getText(), txtModelo.getText(),
                Integer.parseInt(txtAno.getText()), txtCor.getText());
        veiculoDAO.inserirVeiculo(veiculo);
    }
}
