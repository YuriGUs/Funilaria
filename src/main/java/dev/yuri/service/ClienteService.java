package dev.yuri.service;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.DAO.VeiculoDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;

import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO;
    private VeiculoDAO veiculoDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
        this.veiculoDAO = new VeiculoDAO();
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }

    public void excluirCliente(int clienteId) {
        clienteDAO.deletar(clienteId);
    }

    public void editarCliente(Cliente cliente, List<Veiculo> veiculos) {
        clienteDAO.atualizar(cliente, veiculos);
    }

    public void adicionarCliente(Cliente cliente, List<Veiculo> veiculos) {
        // Chama o mét0do do DAO para adicionar o cliente
        clienteDAO.salvar(cliente, veiculos);

        // Agora chama o VeiculoDAO para adicionar os veículos, caso existam
        for (Veiculo veiculo : veiculos) {
            veiculoDAO.inserirVeiculo(veiculo);
        }
    }

}
