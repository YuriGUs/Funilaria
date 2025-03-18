package dev.yuri.controller;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditarClienteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpfCnpj;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtTelefone;
    @FXML private TableView<Veiculo> tableVeiculos; // Substitui VBox por TableView
    @FXML private TableColumn<Veiculo, String> colPlaca;
    @FXML private TableColumn<Veiculo, String> colModelo;
    @FXML private TableColumn<Veiculo, Integer> colAno;
    @FXML private TableColumn<Veiculo, String> colCor;
    @FXML private Button btnSalvar;
    @FXML private VBox painelVeiculo;
    @FXML private TextField campoPlaca, campoModelo, campoAno, campoCor;
    @FXML private TableColumn<Veiculo, Void> colExcluir; // Tipo Void porque não exibe dados do Veiculo**
    @FXML private Button btnCancelarVeiculo;

    private Cliente cliente;
    private List<Veiculo> veiculos; // Lista direta de Veiculo

    // Inicialização do controller (opcional, para configurar a tabela)
    @FXML
    private void initialize() {
        // Configura as colunas da tabela
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colCor.setCellValueFactory(new PropertyValueFactory<>("cor"));

        // Torna as colunas editáveis
        colPlaca.setCellFactory(TextFieldTableCell.forTableColumn());
        colModelo.setCellFactory(TextFieldTableCell.forTableColumn());
        colAno.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCor.setCellFactory(TextFieldTableCell.forTableColumn());

        // Atualiza os valores ao editar
        colPlaca.setOnEditCommit(event -> event.getRowValue().setPlaca(event.getNewValue()));
        colModelo.setOnEditCommit(event -> event.getRowValue().setModelo(event.getNewValue()));
        colAno.setOnEditCommit(event -> event.getRowValue().setAno(event.getNewValue()));
        colCor.setOnEditCommit(event -> event.getRowValue().setCor(event.getNewValue()));

        // Configurando a coluna "Excluir" com botões
        colExcluir.setCellFactory(column -> {
            TableCell<Veiculo, Void> cell = new TableCell<>() {
                private final Button btnExcluir = new Button("Excluir");

                {
                    btnExcluir.setOnAction(event -> {
                        Veiculo veiculo = getTableView().getItems().get(getIndex());
                        veiculos.remove(veiculo);
                        getTableView().getItems().remove(veiculo);
                    });
                    btnExcluir.setStyle(
                            "-fx-background-color: #f44336; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-padding: 5px 10px; " + // Reduz o padding para deixar o botão mais fino
                                    "-fx-pref-width: 70px;"      // Define uma largura fixa menor
                    );
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnExcluir);
                    }
                }
            };
            return cell;
        });
    }

    // Mét0do para adicionar um novo veículo
    @FXML
    private void adicionarVeiculo() {
        painelVeiculo.setVisible(true);
    }

    // Mét0do para salvar o veículo
    @FXML
    private synchronized void salvarVeiculo() {
        String placa = campoPlaca.getText();
        String modelo = campoModelo.getText();
        String anoStr = campoAno.getText();
        String cor = campoCor.getText();

        if (placa.isEmpty() || modelo.isEmpty() || anoStr.isEmpty() || cor.isEmpty()) {
            System.out.println("Todos os campos do veículo devem ser preenchidos!");
            return;
        }

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido!");
            return;
        }

        Veiculo novoVeiculo = new Veiculo(0, placa, modelo, ano, cor, cliente.getId());
        veiculos.add(novoVeiculo);
        tableVeiculos.getItems().add(novoVeiculo);

        campoPlaca.clear();
        campoModelo.clear();
        campoAno.clear();
        campoCor.clear();
        painelVeiculo.setVisible(false);
    }

    // Mét0do para configurar o cliente e seus veículos
    public void setCliente(Cliente cliente, List<Veiculo> veiculos) {
        this.cliente = cliente;
        this.veiculos = veiculos != null ? new ArrayList<>(veiculos) : new ArrayList<>();
        txtNome.setText(cliente.getNome());
        txtCpfCnpj.setText(cliente.getCpfCnpj());
        txtEndereco.setText(cliente.getEndereco());
        txtTelefone.setText(cliente.getTelefone());
        carregarVeiculos();
    }

    // Mét0do para carregar os veículos na tabela
    private void carregarVeiculos() {
        tableVeiculos.getItems().clear();
        tableVeiculos.getItems().addAll(veiculos);
    }

    // Mét0do para salvar as edições do cliente e seus veículos
    @FXML
    private synchronized void salvarEdicao() {
        if (txtNome.getText().isEmpty() || txtCpfCnpj.getText().isEmpty() || txtEndereco.getText().isEmpty() || txtTelefone.getText().isEmpty()) {
            System.out.println("Todos os campos do cliente devem ser preenchidos!");
            return;
        }

        cliente.setNome(txtNome.getText());
        cliente.setCpfCnpj(txtCpfCnpj.getText());
        cliente.setEndereco(txtEndereco.getText());
        cliente.setTelefone(txtTelefone.getText());

        // Os veículos já estão atualizados na lista veiculos devido à edição direta na tabela
        try {
            new ClienteDAO().atualizar(cliente, veiculos);
            System.out.println("Cliente e veículos salvos com sucesso!");
            fecharJanela();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar as edições: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Mét0do para fechar a janela de edição
    private void fecharJanela() {
        Stage stage = (Stage) btnSalvar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelarVeiculo() {
        campoPlaca.clear();
        campoModelo.clear();
        campoAno.clear();
        campoCor.clear();
        painelVeiculo.setVisible(false);
    }
}