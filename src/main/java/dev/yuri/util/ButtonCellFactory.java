package dev.yuri.util;

import dev.yuri.DAO.ItemOrcamentoDAO;
import dev.yuri.controller.EditarItemController;
import dev.yuri.model.ItemOrcamento;
import dev.yuri.service.OrcamentoService;
import javafx.scene.control.*;
import javafx.util.Callback;

public class ButtonCellFactory<S> implements Callback<TableColumn<S, Void>, TableCell<S, Void>> {
    @Override
    public TableCell<S, Void> call(final TableColumn<S, Void> param) {
        return new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");

            {
                btnEditar.setOnAction(event -> {
                    S item = getTableView().getItems().get(getIndex());
                    if (item instanceof ItemOrcamento itemOrcamento) {
                        EditarItemController.abrirJanelaEdicao(itemOrcamento);

                        // Após edição, atualizar o valor total do orçamento
                        OrcamentoService service = new OrcamentoService();
                        int idOrcamento = itemOrcamento.getIdOrcamento();
                        var itens = service.listarItensPorOrcamento(idOrcamento);
                        double novoTotal = itens.stream().mapToDouble(ItemOrcamento::getValorTotal).sum();
                        service.atualizarValorTotal(idOrcamento, novoTotal);

                        getTableView().refresh();
                    }
                });

                btnExcluir.setOnAction(event -> {
                    S item = getTableView().getItems().get(getIndex());
                    if (item instanceof ItemOrcamento) {
                        ItemOrcamento itemOrcamento = (ItemOrcamento) item;

                        // Criar o alerta de confirmação
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmação de Exclusão");
                        alert.setHeaderText("Você tem certeza que deseja excluir este item?");
                        alert.setContentText("Essa ação não pode ser desfeita.");

                        // Exibir o alerta e esperar pela resposta do usuário
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                // Se o usuário confirmar a exclusão
                                new OrcamentoService().deletarItensPorOrcamento(itemOrcamento.getId());
                                getTableView().getItems().remove(itemOrcamento);
                            } else {
                                // Caso o usuário cancele, nada acontece
                                System.out.println("Exclusão cancelada");
                            }
                        });
                    }
                });

                btnEditar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                btnExcluir.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new javafx.scene.layout.HBox(5, btnEditar, btnExcluir));
                }
            }
        };
    }
}
