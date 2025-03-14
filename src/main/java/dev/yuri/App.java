package dev.yuri;

import dev.yuri.Util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        new Thread(() -> {
            DatabaseConnection.criarTabelas(); // Cria as tabelas caso não exista

            // Testando conexão com o banco de dados
            DatabaseConnection.inserirCliente("João da Silva", "155545678901", "Rua A, 123", "(11) 98765-4321");
        }).start();


        // Carrega interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/cadastro_cliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400); // Tela principal
        stage.setTitle("Sistema de funilaria");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
