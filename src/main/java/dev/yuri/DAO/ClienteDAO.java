package dev.yuri.DAO;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Mét0do para salvar cliente e vincular veículos
    public void salvar(Cliente cliente, List<Veiculo> veiculos) {
        String sqlVerificaCliente = "SELECT COUNT(*) FROM clientes WHERE cpf_cnpj = ?";
        String sqlCliente = "INSERT INTO clientes (nome, cpf_cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";
        String sqlVeiculo = "INSERT INTO veiculos (cliente_id, placa, modelo, ano, cor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()) {
            // Iniciar transação
            conn.setAutoCommit(false);

            // Verificar se o CPF/CNPJ já existe
            try (PreparedStatement pstmtVerifica = conn.prepareStatement(sqlVerificaCliente)) {
                pstmtVerifica.setString(1, cliente.getCpfCnpj());
                try (ResultSet rs = pstmtVerifica.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Erro: CPF/CNPJ já está cadastrado.");
                        return; // Retorna sem salvar o cliente
                    }
                }
            }


            try (PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
                pstmtCliente.setString(1, cliente.getNome());
                pstmtCliente.setString(2, cliente.getCpfCnpj());
                pstmtCliente.setString(3, cliente.getEndereco());
                pstmtCliente.setString(4, cliente.getTelefone());

                // Tenta salvar o cliente
                int rowsAffected = pstmtCliente.executeUpdate();

                if (rowsAffected > 0) {
                    // Obtém o ID do cliente gerado pela inserção usando last_insert_rowid()
                    String idSql = "SELECT last_insert_rowid()";
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(idSql)) {
                        if (rs.next()) {
                            int clienteId = rs.getInt(1);
                            cliente.setId(clienteId);
                            System.out.println("Cliente salvo com ID: " + clienteId);

                            // Agora que o cliente foi salvo, insira os veículos
                            try (PreparedStatement pstmtVeiculo = conn.prepareStatement(sqlVeiculo)) {
                                for (Veiculo veiculo : veiculos) {
                                    pstmtVeiculo.setInt(1, clienteId);
                                    pstmtVeiculo.setString(2, veiculo.getPlaca());
                                    pstmtVeiculo.setString(3, veiculo.getModelo());
                                    pstmtVeiculo.setLong(4, veiculo.getAno());
                                    pstmtVeiculo.setString(5, veiculo.getCor());
                                    pstmtVeiculo.addBatch();  // Adiciona ao lote
                                }
                                pstmtVeiculo.executeBatch();  // Executa todos os comandos de uma vez
                            }

                            // Commitar transação
                            conn.commit();
                            System.out.println("Transação completa: Cliente e veículos salvos.");
                        } else {
                            conn.rollback(); // Realiza o rollback se o cliente não foi salvo corretamente
                            System.out.println("Erro: ID do cliente não foi gerado!");
                        }
                    }
                } else {
                    conn.rollback();
                    System.out.println("Erro ao salvar cliente: Nenhuma linha afetada.");
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao salvar cliente e veículos: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar cliente e veículos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Atualizar um veículo
    private void atualizarVeiculo(Veiculo veiculo, Connection conn) throws SQLException {
        String sql = "UPDATE veiculos SET placa = ?, modelo = ?, ano = ?, cor = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, veiculo.getPlaca());
            pstmt.setString(2, veiculo.getModelo());
            pstmt.setLong(3, veiculo.getAno());
            pstmt.setString(4, veiculo.getCor());
            pstmt.setInt(5, veiculo.getId());
            pstmt.executeUpdate();
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf_cnpj"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                );
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public void deletar(int clienteId) {
        String sqlVeiculos = "DELETE FROM veiculos WHERE cliente_id = ?";
        String sqlCliente = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            // Excluir veículos associados ao cliente
            try (PreparedStatement pstmtVeiculos = conn.prepareStatement(sqlVeiculos)) {
                pstmtVeiculos.setInt(1, clienteId);
                pstmtVeiculos.executeUpdate();
            }

            // Excluir cliente
            try (PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
                pstmtCliente.setInt(1, clienteId);
                pstmtCliente.executeUpdate();
                System.out.println("Cliente excluído com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    public List<Veiculo> obterVeiculosPorCliente(int clienteId) {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculos WHERE cliente_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Veiculo veiculo = new Veiculo(
                        rs.getInt("id"),
                        rs.getInt("cliente_id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("cor")
                );
                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter veículos: " + e.getMessage());
        }
        return veiculos;
    }

    public void atualizar(Cliente cliente, List<Veiculo> veiculos) {
        String sqlCliente = "UPDATE clientes SET nome = ?, cpf_cnpj = ?, endereco = ?, telefone = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false);

            // Atualizar cliente
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCliente)) {
                pstmt.setString(1, cliente.getNome());
                pstmt.setString(2, cliente.getCpfCnpj());
                pstmt.setString(3, cliente.getEndereco());
                pstmt.setString(4, cliente.getTelefone());
                pstmt.setInt(5, cliente.getId());

                pstmt.executeUpdate();

                // Atualizar ou inserir veículos
                for (Veiculo veiculo : veiculos) {
                    if (veiculo.getId() == 0) {
                        inserirVeiculo(veiculo, cliente.getId(), conn); // Inserir novo veículo
                    } else {
                        atualizarVeiculo(veiculo, conn); // Atualizar veículo existente
                    }
                }

                // Commitar transação
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao atualizar cliente e veículos: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    private void inserirVeiculo(Veiculo veiculo, int clienteId, Connection conn) throws SQLException {
        String sql = "INSERT INTO veiculos (cliente_id, placa, modelo, ano, cor) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setString(2, veiculo.getPlaca());
            pstmt.setString(3, veiculo.getModelo());
            pstmt.setLong(4, veiculo.getAno());
            pstmt.setString(5, veiculo.getCor());
            pstmt.executeUpdate();
        }
    }
}
