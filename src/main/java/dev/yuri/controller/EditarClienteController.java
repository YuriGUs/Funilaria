package dev.yuri.controller;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditarClienteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpfCnpj;
    @FXML private TextField txtEndereco;
    @FXML private TextField txtTelefone;
    @FXML private VBox vboxVeiculos;  // Caixa onde os campos dos veículos serão exibidos
    @FXML private Button btnSalvar;
    @FXML private AnchorPane painelVeiculo;
    @FXML private TextField campoPlaca, campoModelo, campoAno, campoCor;

    private Cliente cliente;
    private List<VeiculoWrapper> veiculoWrappers = new ArrayList<>();

    // Mét0do para adicionar um novo veículo
    @FXML
    private void adicionarVeiculo() {
        painelVeiculo.setVisible(true); // Torna o painel visível para inserir os dados do veículo
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
            ano = Integer.parseInt(anoStr);  // Converter para int
        } catch (NumberFormatException e) {
            System.out.println("Ano inválido!");
            return;
        }

        // Criando o novo veículo
        Veiculo novoVeiculo = new Veiculo(0, placa, modelo, ano, cor, cliente.getId());
        veiculoWrappers.add(new VeiculoWrapper(novoVeiculo, campoPlaca, campoModelo, campoAno, campoCor));

        // Limpa e atualiza interface
        campoPlaca.clear();
        campoModelo.clear();
        campoAno.clear();
        campoCor.clear();
        painelVeiculo.setVisible(false); // Oculta o painel após salvar
        carregarVeiculos(getVeiculosFromWrappers()); // Atualiza a exibição dos veículos (todos)
    }

    private List<Veiculo> getVeiculosFromWrappers() {
        List<Veiculo> veiculos = new ArrayList<>();
        for (VeiculoWrapper wrapper : veiculoWrappers) {
            veiculos.add(wrapper.getVeiculo());
        }
        return veiculos;
    }

    // Mét0do para configurar o cliente e seus veículos
    public void setCliente(Cliente cliente, List<Veiculo> veiculos) {
        this.cliente = cliente;
        txtNome.setText(cliente.getNome());
        txtCpfCnpj.setText(cliente.getCpfCnpj());
        txtEndereco.setText(cliente.getEndereco());
        txtTelefone.setText(cliente.getTelefone());
        carregarVeiculos(veiculos != null ? veiculos : new ArrayList<>());
    }

    // Mét0do para carregar os veículos dinamicamente na tela
    private void carregarVeiculos(List<Veiculo> veiculos) {
        vboxVeiculos.getChildren().clear();  // Limpar os veículos existentes na interface
        veiculoWrappers.clear();  // Limpar a lista de wrappers (mapeamento de veículo com campos de texto)

        // Adiciona cada veículo à interface com os campos preenchidos
        for (Veiculo veiculo : veiculos) {
            adicionarVeiculo(veiculo); // Preenche os campos com dados existentes do veículo
        }
    }

    // Mét0do para adicionar um veículo com campos de texto para cada propriedade
    private void adicionarVeiculo(Veiculo veiculo) {
        TextField txtPlaca = new TextField(veiculo.getPlaca());
        TextField txtModelo = new TextField(veiculo.getModelo());
        TextField txtAno = new TextField(veiculo.getAno() == 0 ? "" : String.valueOf(veiculo.getAno()));
        TextField txtCor = new TextField(veiculo.getCor());

        // Define o estilo e a estrutura dos campos de veículo
        VBox vboxVeiculo = new VBox(5, txtPlaca, txtModelo, txtAno, txtCor);
        vboxVeiculo.setStyle("-fx-border-color: gray; -fx-padding: 5; -fx-background-color: #f5f5f5;");

        // Adiciona os campos do veículo ao VBox de veículos
        vboxVeiculos.getChildren().add(vboxVeiculo);

        // Mapeia os campos de texto para o veículo atual
        veiculoWrappers.add(new VeiculoWrapper(veiculo, txtPlaca, txtModelo, txtAno, txtCor));
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

        List<Veiculo> veiculosAtualizados = new ArrayList<>();
        for (VeiculoWrapper wrapper : veiculoWrappers) {
            Veiculo veiculo = wrapper.getVeiculo();
            veiculo.setPlaca(wrapper.getTxtPlaca().getText());
            veiculo.setModelo(wrapper.getTxtModelo().getText());
            String anoText = wrapper.getTxtAno().getText();
            veiculo.setAno(anoText.isEmpty() ? 0 : Integer.parseInt(anoText));
            veiculo.setCor(wrapper.getTxtCor().getText());
            veiculosAtualizados.add(veiculo);
        }

        try {
            new ClienteDAO().atualizar(cliente, veiculosAtualizados);  // Atualiza o cliente e os veículos no banco
        } catch (SQLException e) {
            System.out.println("Erro ao salvar as edições: " + e.getMessage()); // TODO trocar para alert
            e.printStackTrace();
        }
        fecharJanela();  // Fecha a janela de edição
    }

    // Mét0do para fechar a janela de edição
    private void fecharJanela() {
        Stage stage = (Stage) btnSalvar.getScene().getWindow();
        stage.close();
    }

    // Wrapper para mapear o veículo com seus campos de texto
    private static class VeiculoWrapper {
        private Veiculo veiculo;
        private TextField txtPlaca, txtModelo, txtAno, txtCor;

        public VeiculoWrapper(Veiculo veiculo, TextField txtPlaca, TextField txtModelo, TextField txtAno, TextField txtCor) {
            this.veiculo = veiculo;
            this.txtPlaca = txtPlaca;
            this.txtModelo = txtModelo;
            this.txtAno = txtAno;
            this.txtCor = txtCor;
        }

        public Veiculo getVeiculo() { return veiculo; }
        public TextField getTxtPlaca() { return txtPlaca; }
        public TextField getTxtModelo() { return txtModelo; }
        public TextField getTxtAno() { return txtAno; }
        public TextField getTxtCor() { return txtCor; }
    }

}
