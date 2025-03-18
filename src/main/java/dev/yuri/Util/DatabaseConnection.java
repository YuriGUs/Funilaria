package dev.yuri.Util;

import java.sql.*;

public class DatabaseConnection {
    private static Connection connection = null;
    private static final String URL = "jdbc:sqlite:C:\\Users\\Yuri\\Desktop\\Sistema\\FunilariaEPintura\\database/funilaria.db";

    // Mét0 do para conectar ao banco de dados
    public static synchronized Connection connect() {
        System.out.println("Tentando conectar ao banco de dados...");
        try {
            // Verifica se a conexão é nula ou está fechada
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Conexão aberta: " + connection);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

        // Mét0do para fechar uma conexão
    public static synchronized void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada: " + connection);
                connection = null;
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Mét0do para criar tabelas no banco de dados
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

        Connection conn = connect();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlClientes);
            System.out.println("Tabela 'clientes' criada/verificada com sucesso!");
            stmt.execute(sqlVeiculos);
            System.out.println("Tabela 'veiculos' criada/verificada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    // Mét0do para inserir um cliente ## não esta sendo utilizado ??
    public static void inserirCliente(String nome, String cpfCnpj, String endereco, String telefone) {
        System.out.println("Inserindo cliente no banco de dados...");
        String sql = "INSERT INTO clientes (nome, cpf_cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";

        Connection conn = connect();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cpfCnpj);
            pstmt.setString(3, endereco);
            pstmt.setString(4, telefone);
            pstmt.executeUpdate();
            System.out.println("Cliente inserido com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }
}