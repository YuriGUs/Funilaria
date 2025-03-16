package dev.yuri.DAO;

import dev.yuri.model.Veiculo;
import dev.yuri.Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public synchronized void salvar(Veiculo veiculo) {
        String sql = "INSERT INTO veiculos (cliente_id, placa, modelo, ano, cor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Desativa o autoCommit

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, veiculo.getClienteId());
                pstmt.setString(2, veiculo.getPlaca());
                pstmt.setString(3, veiculo.getModelo());
                pstmt.setInt(4, veiculo.getAno());
                pstmt.setString(5, veiculo.getCor());
                pstmt.executeUpdate();

                conn.commit(); // Confirma a transação
                System.out.println("Veículo cadastrado com sucesso!");
            } catch (SQLException e) {
                conn.rollback(); // Desfaz a transação em caso de erro
                System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void atualizar(Veiculo veiculo) {
        String sql = "UPDATE veiculos SET placa = ?, modelo = ?, ano = ?, cor = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, veiculo.getPlaca());
            pstmt.setString(2, veiculo.getModelo());
            pstmt.setInt(3, veiculo.getAno());
            pstmt.setString(4, veiculo.getCor());
            pstmt.setInt(5, veiculo.getId());
            pstmt.executeUpdate();

            System.out.println("Veículo atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar veículo: " + e.getMessage());
        }
    }
    // Mét0do para deletar um veículo específico
    public void deletar(Veiculo veiculo) {
        String sql = "DELETE FROM veiculos WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, veiculo.getId());
            pstmt.executeUpdate();

            System.out.println("Veículo excluído com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir veículo: " + e.getMessage());
        }
    }

    public List<Veiculo> listarVeiculosPorCliente(int clienteId) {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculos WHERE cliente_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Veiculo veiculo = new Veiculo(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("cor"),
                        rs.getInt(clienteId)
                );
                veiculos.add(veiculo);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar veículos: " + e.getMessage());
        }
        return veiculos;
    }

    public void excluirVeiculosPorCliente(int clienteId) {
        String sql = "DELETE FROM veiculos WHERE cliente_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
            System.out.println("Veículos do cliente com ID " + clienteId + " excluídos com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao excluir veículos: " + e.getMessage());
        }
    }

}
