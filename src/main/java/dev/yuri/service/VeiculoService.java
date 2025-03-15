package dev.yuri.service;

import dev.yuri.model.Veiculo;
import dev.yuri.DAO.VeiculoDAO;

import java.util.List;

public class VeiculoService {

    private VeiculoDAO veiculoDAO;

    public VeiculoService() {
        this.veiculoDAO = new VeiculoDAO();
    }

    // Listar veículos por cliente
    public List<Veiculo> listarVeiculosPorCliente(int clienteId) {
        return veiculoDAO.listarVeiculosPorCliente(clienteId);
    }

    // Adicionar um novo veículo
    public void adicionarVeiculo(Veiculo veiculo) {
        veiculoDAO.salvar(veiculo);
    }

    // Editar um veículo existente
    public void editarVeiculo(Veiculo veiculo) {
        veiculoDAO.atualizar(veiculo);
    }

    // Excluir um veículo
    public void excluirVeiculo(Veiculo veiculo) {
        veiculoDAO.deletar(veiculo);
    }

    // Excluir todos os veículos de um cliente (quando o cliente for excluído)
    public void excluirVeiculosPorCliente(int clienteId) {
        veiculoDAO.excluirVeiculosPorCliente(clienteId);
    }
}
