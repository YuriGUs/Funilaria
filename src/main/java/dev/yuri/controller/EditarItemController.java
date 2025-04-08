package dev.yuri.controller;

import dev.yuri.DAO.ItemOrcamentoDAO;
import dev.yuri.model.ItemOrcamento;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarItemController {

    @FXML private TextField txtQuantidade;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtValorUnitario;
    @FXML private TextField txtResponsavel;

    private ItemOrcamento item;

    public void setItem(ItemOrcamento item) {
        this.item = item;
        txtQuantidade.setText(String.valueOf(item.getQuantidade()));
        txtDescricao.setText(item.getDescricao());
        txtValorUnitario.setText(String.valueOf(item.getValorUnitario()));
        txtResponsavel.setText(item.getResponsavel());
    }

    @FXML
    public void salvar() {
        item.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
        item.setDescricao(txtDescricao.getText());
        item.setValorUnitario(Double.parseDouble(txtValorUnitario.getText()));
        item.setResponsavel(txtResponsavel.getText());

        new ItemOrcamentoDAO().atualizar(item);

        // Fechar a janela
        ((Stage) txtQuantidade.getScene().getWindow()).close();
    }

    @FXML
    public void cancelar() {
        ((Stage) txtQuantidade.getScene().getWindow()).close();
    }

    public static void abrirJanelaEdicao(ItemOrcamento item) {
        try {
            FXMLLoader loader = new FXMLLoader(EditarItemController.class.getResource("/View/editarItem.fxml"));
            Parent root = loader.load();

            EditarItemController controller = loader.getController();
            controller.setItem(item);

            Stage stage = new Stage();
            stage.setTitle("Editar Item");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait(); // Espera o usuário fechar

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
