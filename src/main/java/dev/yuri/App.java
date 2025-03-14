package dev.yuri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Criando o menu lateral (VBox)
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setPrefWidth(230);
        menu.setStyle("-fx-background-color: #2C3E50;");

        // Criando os botões do menu
        Button btn1 = new Button("Cadastro");
        Button btn2 = new Button("Cadastro de veículo");
        Button btn3 = new Button("Orçamento");

        // Definindo o tamanho dos botões
        btn1.setPrefSize(200, 50);
        btn2.setPrefSize(200, 50);
        btn3.setPrefSize(200, 50);

        // Adicionando ação nos botões
        StackPane content = new StackPane();
        btn1.setOnAction(e -> carregarTela(content, "/view/cadastro.fxml"));
        btn2.setOnAction(e -> mostrarConteudo(content, "Cadastro de Veículo"));
        btn3.setOnAction(e -> mostrarConteudo(content, "Orçamento"));

        // Adicionando botões ao menu
        menu.getChildren().addAll(btn1, btn2, btn3);

        // Criando o layout principal (BorderPane)
        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(content);

        // Criando e configurando a cena
        Scene scene = new Scene(root, 1024, 700);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("Sistema de Funilaria");
        stage.show();
    }

    // Mét0do para carregar um FXML dentro do StackPane
    private void carregarTela(StackPane content, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            content.getChildren().clear();
            content.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Mét0do para exibir o conteúdo ao lado do menu
    private void mostrarConteudo(StackPane content, String texto) {
        content.getChildren().clear();
        Button button = new Button(texto);
        content.getChildren().add(button);
    }

    public static void main(String[] args) {
        launch();
    }
}
