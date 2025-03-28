package dev.yuri.DAO;

import dev.yuri.Util.DatabaseConnection;
import dev.yuri.model.Orcamento;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class OrcamentoDAO {

    public int salvar(Orcamento orcamento) {
        String sql = "INSERT INTO orcamentos (id_cliente, id_veiculo, data, valor_total) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orcamento.getIdCliente());
            stmt.setInt(2, orcamento.getIdVeiculo());

            // Converta para String no formato ISO
            String dataStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(orcamento.getData());
            stmt.setString(3, dataStr);

            stmt.setDouble(4, orcamento.getValorTotal());
            stmt.executeUpdate();

            // Obtenha o ID inserido
            try (Statement stmtId = conn.createStatement();
                 ResultSet rs = stmtId.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void atualizarValorTotal(int idOrcamento, double valorTotal) {
        String sql = "UPDATE orcamentos SET valor_total = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, valorTotal);
            stmt.setInt(2, idOrcamento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Orcamento> listarTodosOrcamentos() {
        List<Orcamento> orcamentos = new ArrayList<>();
        String sql = "SELECT id, id_cliente, id_veiculo, data, valor_total FROM orcamentos";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // String do banco para Date
                Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(rs.getString("data"));

                Orcamento orcamento = new Orcamento(
                        rs.getInt("id_cliente"),
                        rs.getInt("id_veiculo"),
                        data,
                        rs.getDouble("valor_total")
                );
                orcamento.setId(rs.getInt("id"));
                orcamentos.add(orcamento);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orcamentos;
    }

    public Orcamento buscarPorId(int idOrcamento) {
        String sql = "SELECT id, id_cliente, id_veiculo, data, valor_total FROM orcamentos WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Orcamento orcamento = new Orcamento(
                        rs.getInt("id_cliente"),
                        rs.getInt("id_veiculo"),
                        new java.util.Date(rs.getTimestamp("data").getTime()),
                        rs.getDouble("valor_total")
                );
                orcamento.setId(rs.getInt("id"));
                return orcamento;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int buscarIdPorClienteEVeiculo(int idCliente, int idVeiculo) {
        String sql = "SELECT id FROM orcamentos WHERE id_cliente = ? AND id_veiculo = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.setInt(2, idVeiculo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


}