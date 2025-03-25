package dev.yuri.service;

import dev.yuri.DAO.ItemOrcamentoDAO;
import dev.yuri.model.ItemOrcamento;

import java.util.List;

public class OrcamentoService {
    private final ItemOrcamentoDAO itemOrcamentoDAO = new ItemOrcamentoDAO();

    // Salva um item do orçamento no banco de dados
    public void salvarItem(ItemOrcamento item, int idOrcamento) {
        itemOrcamentoDAO.salvar(item, idOrcamento);
    }

    // Lista todos os itens de um orçamento específico
    public List<ItemOrcamento> listarItensPorOrcamento(int idOrcamento) {
        return itemOrcamentoDAO.listarPorOrcamento(idOrcamento);
    }

    // Deleta todos os itens de um orçamento
    public void deletarItensPorOrcamento(int idOrcamento) {
        itemOrcamentoDAO.deletarPorOrcamento(idOrcamento);
    }

    // Calcula o valor total de um orçamento
    public double calcularTotal(List<ItemOrcamento> itens) {
        return itens.stream().mapToDouble(ItemOrcamento::getValorTotal).sum();
    }
}
