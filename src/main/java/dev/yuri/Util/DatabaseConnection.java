package dev.yuri.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:database/funilaria.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }

    // Cria tabela no BD
    public static void criarTabelas() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "cpf_cnpj TEXT NOT NULL UNIQUE, " +
                "endereco TEXT NOT NULL, " +
                "telefone TEXT NOT NULL)";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela criada/verificada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar/verificar tabela: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        // Testando conexão

        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            } else {
                System.out.println("Falha na conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }

        criarTabelas();
    }
}
