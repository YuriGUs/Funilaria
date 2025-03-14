package dev.yuri.DAO;

import dev.yuri.model.Cliente;
import dev.yuri.Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, cpf_cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNome());
            pstmt.setString(2, cliente.getCpfCnpj());
            pstmt.setString(3, cliente.getEndereco());
            pstmt.setString(4, cliente.getTelefone());
            pstmt.executeUpdate();

            // Pega o ID gerado
            int id = (int) conn.createStatement().executeQuery("SELECT last_insert_rowid()").getInt(1);
            cliente.setId(id);

            System.out.println("Cliente cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf_cnpj"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                );
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public void excluirCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Cliente excluído com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }

}
