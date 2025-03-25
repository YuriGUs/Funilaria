package dev.yuri.controller;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.model.ItemOrcamento;
import dev.yuri.service.OrcamentoService;
import dev.yuri.service.ClienteService;
import dev.yuri.service.VeiculoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrcamentoController {

    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<Veiculo> comboVeiculos;
    @FXML private TableView<ItemOrcamento> tableOrcamento;

    @FXML private TableColumn<ItemOrcamento, Integer> colunaQuantidade;
    @FXML private TableColumn<ItemOrcamento, String> colunaDescricao;
    @FXML private TableColumn<ItemOrcamento, Double> colunaValorUnitario;
    @FXML private TableColumn<ItemOrcamento, Double> colunaValorTotal;
    @FXML private TableColumn<ItemOrcamento, String> colunaResponsavel;

    @FXML private TextField txtQuantidade, txtDescricao, txtValorUnitario, txtResponsavel;
    @FXML private Button btnAdicionarServico, btnImprimirOrcamento;

    private final ObservableList<ItemOrcamento> listaServicos = FXCollections.observableArrayList();

    private final OrcamentoService orcamentoService = new OrcamentoService();
    private final ClienteService clienteService = new ClienteService();
    private final VeiculoService veiculoService = new VeiculoService();

    private int idOrcamentoAtual = 0; // Para associar os serviços ao orçamento correto

    @FXML
    public void initialize() {
        carregarClientes();

        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colunaValorUnitario.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        colunaValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colunaResponsavel.setCellValueFactory(new PropertyValueFactory<>("responsavel"));

        tableOrcamento.setItems(listaServicos);
    }

    private void carregarClientes() {
        comboClientes.setItems(FXCollections.observableArrayList(clienteService.listarClientes()));
    }

    @FXML
    private void selecionarCliente() {
        Cliente clienteSelecionado = comboClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            comboVeiculos.setItems(FXCollections.observableArrayList(veiculoService.listarPorCliente(clienteSelecionado.getId())));
        }
    }

    @FXML
    private void adicionarServico() {
        try {
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            String descricao = txtDescricao.getText();
            double valorUnitario = Double.parseDouble(txtValorUnitario.getText());
            String responsavel = txtResponsavel.getText();
            double valorTotal = quantidade * valorUnitario;

            ItemOrcamento servico = new ItemOrcamento(quantidade, descricao, valorUnitario, responsavel);

            // Salva no banco
            orcamentoService.salvarItem(servico, idOrcamentoAtual);

            // Atualiza a tabela
            listaServicos.add(servico);
            tableOrcamento.refresh();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Preencha os campos corretamente!", ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void imprimirOrcamento() {
        System.out.println("Imprimindo orçamento...");
    }
}
