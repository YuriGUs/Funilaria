package dev.yuri.controller;

import dev.yuri.DAO.OrcamentoDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Orcamento;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int idOrcamentoAtual = -1;

    private Map<Integer, Integer> orcamentosPorCliente = new HashMap<>();


    @FXML
    public void initialize() {
        configurarComboBoxes();
        configurarTabela();
        carregarClientes();
        carregarOrcamentosExistentes();
        tableOrcamento.setItems(listaServicos); // teste
    }

    @FXML
    private void selecionarCliente() {
        Cliente clienteSelecionado = comboClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            comboVeiculos.setItems(FXCollections.observableArrayList(
                    veiculoService.listarVeiculosPorCliente(clienteSelecionado.getId())
            ));

            // Verifica se já existe um orçamento para este cliente
            idOrcamentoAtual = orcamentosPorCliente.getOrDefault(clienteSelecionado.getId(), -1);

            if (idOrcamentoAtual != -1) {
                carregarItensOrcamento();
            } else {
                listaServicos.clear();
            }
        }
    }
    @FXML
    private void adicionarServico() {
        if (comboClientes.getValue() == null || comboVeiculos.getValue() == null) {
            mostrarAlerta("Atenção", "Selecione um cliente e um veículo primeiro!", Alert.AlertType.WARNING);
            return;
        }

        if (comboClientes.getSelectionModel().isEmpty() || comboVeiculos.getSelectionModel().isEmpty()) {
            mostrarAlerta("Atenção", "Selecione um cliente e um veículo!", Alert.AlertType.WARNING);
            return;
        }

        if (!validarCamposServico()) {
            return;
        }

        if (idOrcamentoAtual == -1) {
            criarNovoOrcamento();
            if (idOrcamentoAtual == -1) return;
        }

        adicionarItemAoOrcamento();
    }
    @FXML
    private void imprimirOrcamento() {
        if (listaServicos.isEmpty()) {
            mostrarAlerta("Atenção", "Nenhum serviço adicionado ao orçamento!", Alert.AlertType.WARNING);
            return;
        }

        // Lógica de impressão aqui
        System.out.println("Imprimindo orçamento ID: " + idOrcamentoAtual);
        mostrarAlerta("Sucesso", "Orçamento impresso com sucesso!", Alert.AlertType.INFORMATION);
    }
    @FXML
    private void adicionarItemAoOrcamento() {
        try {
            ItemOrcamento item = new ItemOrcamento(
                    idOrcamentoAtual,
                    Integer.parseInt(txtQuantidade.getText()),
                    txtDescricao.getText(),
                    Double.parseDouble(txtValorUnitario.getText()),
                    txtResponsavel.getText()
            );

            adicionarItemAoOrcamento(item); // Reutiliza o novo método
            limparCamposServico();

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Valores inválidos!", Alert.AlertType.ERROR);
        }
    }

    private void carregarOrcamentosExistentes() {
        List<Orcamento> orcamentos = orcamentoService.listarTodosOrcamentos();
        for (Orcamento orcamento : orcamentos) {
            orcamentosPorCliente.put(orcamento.getIdCliente(), orcamento.getId());
        }
    }

    private void configurarComboBoxes() {
        // Configuração do ComboBox de clientes
        comboClientes.setCellFactory(param -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome() + " - " + item.getCpfCnpj());
            }
        });

        comboClientes.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome() + " - " + item.getCpfCnpj());
            }
        });

        // Configuração do ComboBox de veículos
        comboVeiculos.setCellFactory(param -> new ListCell<Veiculo>() {
            @Override
            protected void updateItem(Veiculo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getPlaca() + " - " + item.getModelo());
            }
        });

        comboVeiculos.setButtonCell(new ListCell<Veiculo>() {
            @Override
            protected void updateItem(Veiculo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getPlaca() + " - " + item.getModelo());
            }
        });

        // Adicione listeners para ambos ComboBoxes
        comboClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                comboVeiculos.setItems(FXCollections.observableArrayList(
                        veiculoService.listarVeiculosPorCliente(newVal.getId())
                ));
                // Limpa a seleção do veículo e a tabela quando muda o cliente
                comboVeiculos.getSelectionModel().clearSelection();
                listaServicos.clear();
                idOrcamentoAtual = -1;
            }
        });

        comboVeiculos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && comboClientes.getValue() != null) {
                // Só carrega o orçamento quando ambos estão selecionados
                carregarOrcamentoExistente();
            }
        });
    }

    private void carregarOrcamentoExistente() {
        int idCliente = comboClientes.getValue().getId();
        int idVeiculo = comboVeiculos.getValue().getId();

        // Verifica se existe orçamento para este cliente e veículo
        idOrcamentoAtual = orcamentoService.buscarIdOrcamentoPorClienteEVeiculo(idCliente, idVeiculo);

        if (idOrcamentoAtual != -1) {
            listaServicos.setAll(orcamentoService.listarItensPorOrcamento(idOrcamentoAtual));
        } else {
            listaServicos.clear();
        }
    }

    private void configurarTabela() {
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
    // MODIFICADO PARA TESTE
    private void carregarItensOrcamento() {
        if (idOrcamentoAtual != -1) {
            System.out.println("DEBUG - Carregando itens para orçamento ID: " + idOrcamentoAtual);

            // Verifica se o orçamento existe
            Orcamento orcamento = orcamentoService.buscarOrcamentoPorId(idOrcamentoAtual);
            if (orcamento == null) {
                System.out.println("DEBUG - Orçamento não encontrado no banco!");
                listaServicos.clear();
                return;
            }

            List<ItemOrcamento> itens = orcamentoService.listarItensPorOrcamento(idOrcamentoAtual);
            System.out.println("DEBUG - Itens recuperados do banco: " + itens.size());

            listaServicos.setAll(itens);
            System.out.println("DEBUG - Itens na listaServicos após carregamento: " + listaServicos.size());
        } else {
            listaServicos.clear();
        }
    }

    private boolean validarCamposServico() {
        if (txtQuantidade.getText().isEmpty() || txtDescricao.getText().isEmpty() ||
                txtValorUnitario.getText().isEmpty() || txtResponsavel.getText().isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos!", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Integer.parseInt(txtQuantidade.getText());
            Double.parseDouble(txtValorUnitario.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Valores numéricos inválidos!", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void criarNovoOrcamento() {
        int idCliente = comboClientes.getSelectionModel().getSelectedItem().getId();
        int idVeiculo = comboVeiculos.getSelectionModel().getSelectedItem().getId();

        Orcamento orcamento = new Orcamento(
                idCliente,
                idVeiculo,
                new java.util.Date(), // Data atual
                0.0
        );

        idOrcamentoAtual = new OrcamentoDAO().salvar(orcamento);
        orcamentosPorCliente.put(idCliente, idOrcamentoAtual);

        if (idOrcamentoAtual == -1) {
            mostrarAlerta("Erro", "Falha ao criar orçamento!", Alert.AlertType.ERROR);
        } else {
            System.out.println("Novo orçamento criado em: " + orcamento.getDataFormatada());
        }
    }

    private void limparCamposServico() {
        txtQuantidade.clear();
        txtDescricao.clear();
        txtValorUnitario.clear();
        txtResponsavel.clear();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, mensagem, ButtonType.OK);
        alert.setTitle(titulo);
        alert.show();
    }

    public void adicionarItemAoOrcamento(ItemOrcamento item) {
        try {
            orcamentoService.salvarItem(item, idOrcamentoAtual);
            listaServicos.add(item); // Adiciona diretamente na lista observável
            tableOrcamento.refresh();

            // Atualiza o valor total (como no fluxo manual)
            double novoTotal = listaServicos.stream()
                    .mapToDouble(ItemOrcamento::getValorTotal)
                    .sum();
            orcamentoService.atualizarValorTotal(idOrcamentoAtual, novoTotal);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao adicionar item: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


}