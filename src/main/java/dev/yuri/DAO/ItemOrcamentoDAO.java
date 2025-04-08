package dev.yuri.DAO;

import dev.yuri.util.DatabaseConnection;
import dev.yuri.model.ItemOrcamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemOrcamentoDAO {

    public void salvar(ItemOrcamento item, int idOrcamento) {
        String sql = "INSERT INTO item_orcamento (id_orcamento, quantidade, descricao, " +
                "valor_unitario, valor_total, responsavel) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false); // Desativa auto-commit

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idOrcamento);
                stmt.setInt(2, item.getQuantidade());
                stmt.setString(3, item.getDescricao());
                stmt.setDouble(4, item.getValorUnitario());
                stmt.setDouble(5, item.getValorTotal());
                stmt.setString(6, item.getResponsavel());

                stmt.executeUpdate();
                conn.commit(); // Confirma a transação
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Em caso de erro, faz rollback
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaura auto-commit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<ItemOrcamento> listarPorOrcamento(int idOrcamento) {
        List<ItemOrcamento> itens = new ArrayList<>();
        String sql = "SELECT id, id_orcamento, quantidade, descricao, " +
                "valor_unitario, valor_total, responsavel " +
                "FROM item_orcamento WHERE id_orcamento = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemOrcamento item = new ItemOrcamento(
                        rs.getInt("id_orcamento"),
                        rs.getInt("quantidade"),
                        rs.getString("descricao"),
                        rs.getDouble("valor_unitario"),
                        rs.getString("responsavel")
                );
                item.setId(rs.getInt("id"));
                item.setValorTotal(rs.getDouble("valor_total")); // Garante que o valor total está correto
                itens.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itens;
    }

    public void deletarPorOrcamento(int idItemOrcamento) {
        String sql = "DELETE FROM item_orcamento WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idItemOrcamento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(ItemOrcamento item) {
        String sql = "UPDATE item_orcamento SET quantidade = ?, descricao = ?, valor_unitario = ?, responsavel = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantidade());
            stmt.setString(2, item.getDescricao());
            stmt.setDouble(3, item.getValorUnitario());
            stmt.setString(4, item.getResponsavel());
            stmt.setInt(5, item.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}