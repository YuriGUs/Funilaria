package dev.yuri.DAO;

import dev.yuri.model.Veiculo;
import dev.yuri.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {
    public List<Veiculo> listarVeiculosPorCliente(int clienteId) {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculos WHERE cliente_id = ?";

        Connection conn = DatabaseConnection.connect();
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setInt(1, clienteId);
            try (ResultSet rs = psmt.executeQuery()){
                while (rs.next()) {
                    Veiculo veiculo = new Veiculo(
                            rs.getInt("id"),
                            rs.getString("placa"),
                            rs.getString("modelo"),
                            rs.getInt("ano"),
                            rs.getString("cor"),
                            rs.getInt("cliente_id")
                    );
                    veiculos.add(veiculo);
                }
            }
        } catch (SQLException  e) {
            System.out.println("Erro ao buscar veículos: " + e.getMessage());
        }
        return veiculos;
    }

}
