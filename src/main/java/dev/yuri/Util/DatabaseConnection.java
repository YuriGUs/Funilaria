package dev.yuri.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:C:\\Users\\Yuri\\Desktop\\Sistema\\FunilariaEPintura\\database/funilaria.db";

    // Mét0 do para conectar ao banco de dados
    public static Connection connect() {
        System.out.println("Tentando conectar ao banco de dados...");
        try {
            Connection conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA journal_mode=WAL;"); // Habilita o modo WAL
            }
            System.out.println("Conexão aberta: " + conn);
            return conn;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            throw new RuntimeException("Falha ao conectar ao banco de dados", e);
        }
    }

    // Mét0do para fechar uma conexão
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada: " + conn);
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // Método para criar tabelas no banco de dados
    public static void criarTabelas() {
        System.out.println("Criando tabelas no banco de dados...");

        String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "cpf_cnpj TEXT NOT NULL UNIQUE, " +
                "endereco TEXT NOT NULL, " +
                "telefone TEXT NOT NULL)";

        String sqlVeiculos = "CREATE TABLE IF NOT EXISTS veiculos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cliente_id INTEGER NOT NULL, " +
                "placa TEXT NOT NULL, " +
                "modelo TEXT NOT NULL, " +
                "ano INTEGER NOT NULL, " +
                "cor TEXT NOT NULL, " +
                "FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE)";

        Connection conn = null;
        try {
            conn = connect();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sqlClientes);
                System.out.println("Tabela 'clientes' criada/verificada com sucesso!");

                stmt.execute(sqlVeiculos);
                System.out.println("Tabela 'veiculos' criada/verificada com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        } finally {
            close(conn); // Garante que a conexão seja fechada
        }
    }

    // Método para inserir um cliente
    public static void inserirCliente(String nome, String cpfCnpj, String endereco, String telefone) {
        System.out.println("Inserindo cliente no banco de dados...");
        String sql = "INSERT INTO clientes (nome, cpf_cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = connect();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, cpfCnpj);
                pstmt.setString(3, endereco);
                pstmt.setString(4, telefone);
                pstmt.executeUpdate();
                System.out.println("Cliente inserido com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        } finally {
            close(conn); // Garante que a conexão seja fechada
        }
    }
}