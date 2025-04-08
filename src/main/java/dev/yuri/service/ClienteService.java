package dev.yuri.service;

import dev.yuri.DAO.ClienteDAO;
import dev.yuri.DAO.VeiculoDAO;
import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;

import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }

    public boolean excluirCliente(int clienteId) {
        return clienteDAO.deletar(clienteId);
    }

    public synchronized void adicionarCliente(Cliente cliente, List<Veiculo> veiculos) {
        // Chama o mét0do do DAO para adicionar o cliente
        clienteDAO.salvar(cliente, veiculos);
    }

    public List<Cliente> buscarUltimosClientes(int limite) {
        return clienteDAO.buscarUltimosClientes(limite);
    }

}
