package dev.yuri.DAO;

import dev.yuri.model.Veiculo;
import dev.yuri.Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void inserirVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO veiculos (cliente_id, placa, modelo, ano, cor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, veiculo.getClienteId());
            pstmt.setString(2, veiculo.getPlaca());
            pstmt.setString(3, veiculo.getModelo());
            pstmt.setInt(4, veiculo.getAno());
            pstmt.setString(5, veiculo.getCor());
            pstmt.executeUpdate();

            System.out.println("Veículo cadastrado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
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
                        rs.getInt("cliente_id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("cor")
                );
                veiculos.add(veiculo);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar veículos: " + e.getMessage());
        }
        return veiculos;
    }


}
