package dev.yuri.Util;

import dev.yuri.DAO.UsuarioDAO;

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

        // Tabela de clientes
        String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "cpf_cnpj TEXT NOT NULL UNIQUE, " +
                "endereco TEXT NOT NULL, " +
                "telefone TEXT NOT NULL)";

        // Tabela de veículos
        String sqlVeiculos = "CREATE TABLE IF NOT EXISTS veiculos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cliente_id INTEGER NOT NULL, " +
                "placa TEXT NOT NULL, " +
                "modelo TEXT NOT NULL, " +
                "ano INTEGER NOT NULL, " +
                "cor TEXT NOT NULL, " +
                "FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE)";

        // Tabela de usuários
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)";

        // Tabela de orçamentos (nova) -- Sqlite não aceita DATE, tem que usar TEXT
        String sqlOrcamentos = "CREATE TABLE IF NOT EXISTS orcamentos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_cliente INTEGER NOT NULL, " +
                "id_veiculo INTEGER NOT NULL, " +
                "data TEXT NOT NULL, " +
                "valor_total REAL NOT NULL, " +
                "FOREIGN KEY (id_cliente) REFERENCES clientes(id), " +
                "FOREIGN KEY (id_veiculo) REFERENCES veiculos(id))";

        // Tabela de itens de orçamento
        String sqlItemOrcamento = "CREATE TABLE IF NOT EXISTS item_orcamento (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_orcamento INTEGER NOT NULL, " +
                "quantidade INTEGER NOT NULL, " +
                "descricao TEXT NOT NULL, " +
                "valor_unitario REAL NOT NULL, " +
                "valor_total REAL NOT NULL, " +
                "responsavel TEXT NOT NULL, " +
                "FOREIGN KEY (id_orcamento) REFERENCES orcamentos(id) ON DELETE CASCADE)";

        Connection conn = connect();
        try (Statement stmt = conn.createStatement()) {
            // Criar tabelas
            stmt.execute(sqlClientes);
            System.out.println("Tabela 'clientes' criada/verificada com sucesso!");

            stmt.execute(sqlVeiculos);
            System.out.println("Tabela 'veiculos' criada/verificada com sucesso!");

            stmt.execute(sqlUsuarios);
            System.out.println("Tabela 'usuarios' criada/verificada com sucesso!");

            stmt.execute(sqlOrcamentos);
            System.out.println("Tabela 'orcamentos' criada/verificada com sucesso!");

            stmt.execute(sqlItemOrcamento);
            System.out.println("Tabela 'item_orcamento' criada/verificada com sucesso!");

            // Criar usuário admin
            String sqlDeleteAdmin = "DELETE FROM usuarios WHERE username = 'admin'";
            stmt.execute(sqlDeleteAdmin);

            String sqlInsertAdmin = "INSERT INTO usuarios (username, password, role) VALUES ('admin', ?, 'admin')";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertAdmin)) {
                String hashedPassword = new UsuarioDAO().hashedPassword("admin123");
                System.out.println("Hash gerado para admin: " + hashedPassword);
                pstmt.setString(1, hashedPassword);
                pstmt.executeUpdate();
                System.out.println("Usuário admin padrão criado com hash correto.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
            e.printStackTrace();
        }
    }

}