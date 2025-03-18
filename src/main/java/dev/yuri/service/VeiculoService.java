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

}
