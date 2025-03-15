package dev.yuri.controller;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.service.ClienteService;
import dev.yuri.service.VeiculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class ClientesController {

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpfCnpj;
    @FXML private TableColumn<Cliente, String> colEndereco;
    @FXML private TableColumn<Cliente, String> colTelefone;
    @FXML private TableColumn<Cliente, Void> colAcoes;

    @FXML private TableView<Veiculo> tabelaVeiculos;
    @FXML private TableColumn<Veiculo, String> colPlaca;
    @FXML private TableColumn<Veiculo, String> colModelo;
    @FXML private TableColumn<Veiculo, Integer> colAno;
    @FXML private TableColumn<Veiculo, String> colCor;

    private ClienteService clienteService = new ClienteService();
    private VeiculoService veiculoService = new VeiculoService();
    private ObservableList<Cliente> clientesLista = FXCollections.observableArrayList();
    private ObservableList<Veiculo> veiculosLista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColunasClientes();
        carregarClientes();

        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                carregarVeiculos(novo);
            }
        });
    }

    private void configurarColunasClientes() {
        colId.setCellValueFactory(dados -> dados.getValue().idProperty().asObject());
        colNome.setCellValueFactory(dados -> dados.getValue().nomeProperty());
        colCpfCnpj.setCellValueFactory(dados -> dados.getValue().cpfCnpjProperty());
        colEndereco.setCellValueFactory(dados -> dados.getValue().enderecoProperty());
        colTelefone.setCellValueFactory(dados -> dados.getValue().telefoneProperty());

        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox painel = new HBox(5, btnEditar, btnExcluir);

            {
                btnEditar.setOnAction(event -> editarCliente(getTableView().getItems().get(getIndex())));
                btnExcluir.setOnAction(event -> excluirCliente(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(painel);
                }
            }
        });
    }

    private void carregarClientes() {
        clientesLista.setAll(clienteService.listarClientes());
        tabelaClientes.setItems(clientesLista);
    }

    private void carregarVeiculos(Cliente cliente) {
        veiculosLista.setAll(veiculoService.listarVeiculosPorCliente(cliente.getId()));
        tabelaVeiculos.setItems(veiculosLista);
    }

    private void editarCliente(Cliente cliente) {
        System.out.println("Editar cliente: " + cliente.getNome());
        // Lógica de edição de cliente
    }

    private void excluirCliente(Cliente cliente) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText("Tem certeza que deseja excluir este cliente?");
        alerta.setContentText(cliente.getNome());

        if (alerta.showAndWait().get() == ButtonType.OK) {
            clienteService.excluirCliente(cliente.getId());
            carregarClientes();
            veiculosLista.clear();
        }
    }

    @FXML
    private void voltar() {
        // Código para voltar à tela anterior (caso tenha um controlador para navegação entre telas)
        System.out.println("Voltar para tela anterior");
    }
}
