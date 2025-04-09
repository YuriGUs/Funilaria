package dev.yuri.controller;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.service.ClienteService;
import dev.yuri.service.VeiculoService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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
    @FXML private ListView<String> listUltimosClientes;

    private ObservableList<String> ultimosClientes = FXCollections.observableArrayList();

    private ClienteService clienteService;

    public void initialize() {
        listUltimosClientes.setItems(ultimosClientes);

        // Limites de caracteres
        limitarCampo(txtPlaca, 7);
        limitarCampo(txtModelo, 30);
        limitarCampo(txtAno, 4);
        limitarCampo(txtCor, 30);
        limitarCampo(txtNome, 50);
        limitarCampo(txtEndereco, 100);

        // Máscaras
        aplicarMascaraCPF_CNPJ(txtCpfCnpj);
        aplicarMascaraTelefone(txtTelefone);

        // Carregar últimos clientes
        List<Cliente> clientesRecentes = clienteService.buscarUltimosClientes(5);
        for (Cliente cliente : clientesRecentes) {
            List<Veiculo> veiculos = cliente.getVeiculos();
            if (veiculos != null && !veiculos.isEmpty()) {
                String linha = cliente.getNome() + " – " + veiculos.get(0).getPlaca();
                ultimosClientes.add(linha);
            }
        }
    }

    public CadastroController() {
        this.clienteService = new ClienteService();
    }

    @FXML
    public synchronized void cadastrarClienteEVeiculo() {
        // Validação básica de campos
        if (!validarCampos()) return;

        Cliente cliente = new Cliente(
                txtNome.getText(),
                txtCpfCnpj.getText(),
                txtEndereco.getText(),
                txtTelefone.getText()
        );

        Veiculo veiculo = new Veiculo(
                0,
                txtPlaca.getText(),
                txtModelo.getText(),
                Integer.parseInt(txtAno.getText()),
                txtCor.getText(),
                0
        );

        List<Veiculo> veiculos = new ArrayList<>();
        veiculos.add(veiculo);

        clienteService.adicionarCliente(cliente, veiculos);

        String nome = txtNome.getText();
        String placa = txtPlaca.getText();

        if (!nome.isEmpty() && !placa.isEmpty()) {
            ultimosClientes.add(0, nome + " – " + placa);
            if (ultimosClientes.size() > 5) {
                ultimosClientes.remove(5);
            }
        }

        limparCampos();
        showAlert("Sucesso", "Cliente e Veículo cadastrados com sucesso!");
    }


    private void aplicarMascaraCPF_CNPJ(TextField campo) {
        campo.textProperty().addListener((obs, oldValue, newValue) -> {
            String digits = newValue.replaceAll("[^\\d]", "");
            if (digits.length() > 14) digits = digits.substring(0, 14);

            StringBuilder formatted = new StringBuilder();

            if (digits.length() <= 11) { // CPF
                for (int i = 0; i < digits.length(); i++) {
                    if (i == 3 || i == 6) formatted.append(".");
                    if (i == 9) formatted.append("-");
                    formatted.append(digits.charAt(i));
                }
            } else { // CNPJ
                for (int i = 0; i < digits.length(); i++) {
                    if (i == 2 || i == 5) formatted.append(".");
                    if (i == 8) formatted.append("/");
                    if (i == 12) formatted.append("-");
                    formatted.append(digits.charAt(i));
                }
            }

            Platform.runLater(() -> {
                campo.setText(formatted.toString());
                campo.positionCaret(formatted.length());
            });
        });
    }

    private void aplicarMascaraTelefone(TextField campo) {
        campo.textProperty().addListener((obs, oldValue, newValue) -> {
            String digits = newValue.replaceAll("[^\\d]", "");
            if (digits.length() > 11) digits = digits.substring(0, 11);

            StringBuilder formatted = new StringBuilder();

            for (int i = 0; i < digits.length(); i++) {
                if (i == 0) formatted.append("(");
                if (i == 2) formatted.append(") ");
                if (i == 7 && digits.length() > 7) formatted.append("-");
                formatted.append(digits.charAt(i));
            }

            Platform.runLater(() -> {
                campo.setText(formatted.toString());
                campo.positionCaret(formatted.length());
            });
        });
    }

    private void limitarCampo(TextField campo, int maxLength) {
        campo.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= maxLength ? change : null));
    }

    private boolean validarCampos() {
        if (txtNome.getText().isEmpty() || txtCpfCnpj.getText().isEmpty() ||
                txtEndereco.getText().isEmpty() || txtTelefone.getText().isEmpty() ||
                txtPlaca.getText().isEmpty() || txtModelo.getText().isEmpty() ||
                txtAno.getText().isEmpty() || txtCor.getText().isEmpty()) {

            showAlert("Erro", "Todos os campos devem ser preenchidos.");
            return false;
        }

        if (txtNome.getText().length() > 100) {
            showAlert("Erro", "Nome deve ter no máximo 100 caracteres.");
            return false;
        }

        if (!txtCpfCnpj.getText().matches("[0-9./\\-]+") || txtCpfCnpj.getText().length() > 18) {
            showAlert("Erro", "CPF/CNPJ deve conter apenas números, pontos, traços e barras e ter no máximo 18 caracteres.");
            return false;
        }

        if (!txtTelefone.getText().matches("[0-9()\\-\s]+") || txtTelefone.getText().length() > 15) {
            showAlert("Erro", "Telefone deve conter apenas números e caracteres válidos e ter no máximo 15 caracteres.");
            return false;
        }

        if (txtEndereco.getText().length() > 150) {
            showAlert("Erro", "Endereço deve ter no máximo 150 caracteres.");
            return false;
        }

        if (txtCor.getText().length() > 20) {
            showAlert("Erro", "Cor deve ter no máximo 20 caracteres.");
            return false;
        }

        if (!txtAno.getText().matches("\\d{4}")) {
            showAlert("Erro", "Ano deve conter exatamente 4 dígitos numéricos.");
            return false;
        }

        return true;
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


