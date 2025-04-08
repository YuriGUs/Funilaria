package dev.yuri.controller;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.service.ClienteService;
import dev.yuri.service.VeiculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
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
    @FXML private ListView<String> listUltimosClientes;

    private ObservableList<String> ultimosClientes = FXCollections.observableArrayList();

    private ClienteService clienteService;

    public void initialize() {
        listUltimosClientes.setItems(ultimosClientes);

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
        // Criando o Veículo associado ao cliente
        Veiculo veiculo = new Veiculo(
                0,  // O ID do veículo será gerado no banco
                txtPlaca.getText(),
                txtModelo.getText(),
                Integer.parseInt(txtAno.getText()),
                txtCor.getText(),
                0
        );

        List<Veiculo> veiculos = new ArrayList<>();
        veiculos.add(veiculo);

        // Agora sim, salvar Cliente e Veículos juntos
        clienteService.adicionarCliente(cliente, veiculos);

        String nome = txtNome.getText();
        String placa = txtPlaca.getText();

        if (!nome.isEmpty() && !placa.isEmpty()) {
            ultimosClientes.add(0, nome + " – " + placa); // adiciona no topo
            if (ultimosClientes.size() > 5) {
                ultimosClientes.remove(5); // mantém os 5 mais recentes
            }
        }

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


