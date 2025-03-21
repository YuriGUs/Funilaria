package dev.yuri;

import dev.yuri.DAO.UsuarioDAO;
import dev.yuri.Util.DatabaseConnection;
import dev.yuri.model.Usuario;
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
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        carregarTelaLogin();
    }

    private void carregarTelaLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login - Sistema de Funilaria");
        primaryStage.show();
    }

    public static void carregarTelaPrincipal() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setPrefWidth(230);
        menu.setStyle("-fx-background-color: #708090;");

        Button btn1 = new Button("Cadastro");
        Button btn2 = new Button("Lista de clientes");
        Button btn3 = new Button("Orçamento");

        btn1.setPrefSize(200, 50);
        btn2.setPrefSize(200, 50);
        btn3.setPrefSize(200, 50);

        StackPane content = new StackPane();
        btn1.setOnAction(e -> carregarTela(content, "/view/cadastro.fxml"));
        btn2.setOnAction(e -> carregarTela(content, "/view/clientesView.fxml"));
        btn3.setOnAction(e -> mostrarConteudo(content, "Orçamento"));

        menu.getChildren().addAll(btn1, btn2, btn3);

        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(content);

        Scene scene = new Scene(root, 1024, 700);

        // Criando uma nova Stage (caso necessário)
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema de Funilaria");
        primaryStage.setMaximized(true);
    }

    private static void carregarTela(StackPane content, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
            content.getChildren().clear();
            content.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mostrarConteudo(StackPane content, String texto) {
        content.getChildren().clear();
        Button button = new Button(texto);
        content.getChildren().add(button);
    }

    @Override
    public void stop() {
        DatabaseConnection.close();
    }

    public static void main(String[] args) {
        DatabaseConnection.criarTabelas();
        launch();
    }
}
