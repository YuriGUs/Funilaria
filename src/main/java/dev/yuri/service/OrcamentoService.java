package dev.yuri.service;

import dev.yuri.DAO.ItemOrcamentoDAO;
import dev.yuri.DAO.OrcamentoDAO;
import dev.yuri.model.ItemOrcamento;
import dev.yuri.model.Orcamento;

import java.util.Date;
import java.util.List;

public class OrcamentoService {
    private final ItemOrcamentoDAO itemOrcamentoDAO = new ItemOrcamentoDAO();
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();

    public List<Orcamento> listarTodosOrcamentos() {
        return orcamentoDAO.listarTodosOrcamentos();
    }

    public Orcamento buscarOrcamentoPorId(int id) {
        return orcamentoDAO.buscarPorId(id);
    }
    // Salva um item do orçamento no banco de dados
    public void salvarItem(ItemOrcamento item, int idOrcamento) {
        // Garante que o valor total está calculado
        item.setValorTotal(item.getQuantidade() * item.getValorUnitario());

        itemOrcamentoDAO.salvar(item, idOrcamento);

        // Atualiza o valor total do orçamento
        List<ItemOrcamento> itens = listarItensPorOrcamento(idOrcamento);
        double novoTotal = itens.stream()
                .mapToDouble(ItemOrcamento::getValorTotal)
                .sum();
        orcamentoDAO.atualizarValorTotal(idOrcamento, novoTotal);
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

    public int buscarIdOrcamentoPorClienteEVeiculo(int idCliente, int idVeiculo) {
        return orcamentoDAO.buscarIdPorClienteEVeiculo(idCliente, idVeiculo);
    }

    public void atualizarValorTotal(int idOrcamento, double valorTotal) {
        orcamentoDAO.atualizarValorTotal(idOrcamento, valorTotal);
    }

}
