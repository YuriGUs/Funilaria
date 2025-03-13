package dev.yuri.DAO;

import dev.yuri.model.Cliente;
import dev.yuri.Util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClienteDAO {
    public void salvarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, cpf_cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj());
            stmt.setString(3, cliente.getEndereco());
            stmt.setString(4, cliente.getTelefone());

            stmt.executeUpdate();
            System.out.println("Cliente salvo no banco de dados!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar cliente: " + e.getMessage());
        }
    }
}
