package dev.yuri.controller;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.service.ClienteService;
import dev.yuri.service.VeiculoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;

public class CadastroController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpfCnpj;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAno;
    @FXML private TextField txtCor;

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableView<Veiculo> tabelaVeiculos;

    private ClienteService clienteService;
    private VeiculoService veiculoService;

    public CadastroController() {
        this.clienteService = new ClienteService();
        this.veiculoService = new VeiculoService();
    }

    @FXML
    public synchronized void cadastrarClienteEVeiculo() {
        // Validar os campos
        if (txtNome.getText().isEmpty() || txtCpfCnpj.getText().isEmpty() ||
                txtEndereco.getText().isEmpty() || txtTelefone.getText().isEmpty() ||
                txtPlaca.getText().isEmpty() || txtModelo.getText().isEmpty() ||
                txtAno.getText().isEmpty() || txtCor.getText().isEmpty()) {

            showAlert("Erro", "Todos os campos devem ser preenchidos.");
            return;
        }

        // Criando o Cliente (ID será gerado pelo banco)
        Cliente cliente = new Cliente(
                txtNome.getText(),
                txtCpfCnpj.getText(),
                txtEndereco.getText(),
                txtTelefone.getText()
        );

        // pegar id gerado do cliente
        int clienteID = cliente.getId();

        // Criando o Veículo associado ao cliente
        Veiculo veiculo = new Veiculo(
                0,  // O ID do veículo será gerado no banco
                txtPlaca.getText(),
                txtModelo.getText(),
                Integer.parseInt(txtAno.getText()),
                txtCor.getText(),
                clienteID
        );

        List<Veiculo> veiculos = new ArrayList<>();
        veiculos.add(veiculo);

        // Agora sim, salvar Cliente e Veículos juntos
        clienteService.adicionarCliente(cliente, veiculos);

        // Limpar os campos após salvar
        limparCampos();

        showAlert("Sucesso", "Cliente e Veículo cadastrados com sucesso!");
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpfCnpj.clear();
        txtEndereco.clear();
        txtTelefone.clear();
        txtPlaca.clear();
        txtModelo.clear();
        txtAno.clear();
        txtCor.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
