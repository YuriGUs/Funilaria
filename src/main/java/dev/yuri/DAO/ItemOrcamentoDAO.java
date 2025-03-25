package dev.yuri.DAO;

import dev.yuri.Util.DatabaseConnection;
import dev.yuri.model.ItemOrcamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemOrcamentoDAO {

    public void salvar(ItemOrcamento item, int idOrcamento) {
        String sql = "INSERT INTO item_orcamento (id_orcamento, quantidade, descricao, valor_unitario, valor_total, responsavel) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            stmt.setInt(2, item.getQuantidade());
            stmt.setString(3, item.getDescricao());
            stmt.setDouble(4, item.getValorUnitario());
            stmt.setDouble(5, item.getValorTotal());
            stmt.setString(6, item.getResponsavel());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemOrcamento> listarPorOrcamento(int idOrcamento) {
        List<ItemOrcamento> itens = new ArrayList<>();
        String sql = "SELECT quantidade, descricao, valor_unitario, valor_total, responsavel FROM item_orcamento WHERE id_orcamento = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemOrcamento item = new ItemOrcamento(
                        rs.getInt("quantidade"),
                        rs.getString("descricao"),
                        rs.getDouble("valor_unitario"),
                        rs.getString("responsavel")
                );
                itens.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itens;
    }

    public void deletarPorOrcamento(int idOrcamento) {
        String sql = "DELETE FROM item_orcamento WHERE id_orcamento = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOrcamento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
