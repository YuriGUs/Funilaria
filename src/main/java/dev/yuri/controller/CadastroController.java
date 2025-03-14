package dev.yuri.controller;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.DAO.VeiculoDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        String nome = txtNome.getText();
        String cpfCnpj = txtCpfCnpj.getText();
        String endereco = txtEndereco.getText();
        String telefone = txtTelefone.getText();

        if (nome.isEmpty() || cpfCnpj.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!", Alert.AlertType.ERROR);
            return;
        }

        Cliente cliente = new Cliente(nome, cpfCnpj, endereco, telefone);
        clienteDAO.inserirCliente(cliente);

        mostrarAlerta("Sucesso", "Cliente cadastrado com sucesso!", Alert.AlertType.INFORMATION);

        limparCampos();
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpfCnpj.clear();
        txtEndereco.clear();
        txtTelefone.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
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
