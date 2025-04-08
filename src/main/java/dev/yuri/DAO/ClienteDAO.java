package dev.yuri.DAO;

import dev.yuri.model.Cliente;
import dev.yuri.model.Veiculo;
import dev.yuri.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private VeiculoDAO veiculoDAO = new VeiculoDAO();


    // Mét0do para salvar cliente e vincular veículos
    public synchronized void salvar(Cliente cliente, List<Veiculo> veiculos) {
        String sqlVerificaCliente = "SELECT COUNT(*) FROM clientes WHERE cpf_cnpj = ?";
        String sqlCliente = "INSERT INTO clientes (nome, cpf_cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";
        String sqlVeiculo = "INSERT INTO veiculos (cliente_id, placa, modelo, ano, cor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Inicia transação

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

            // Tenta salvar o cliente
            try (PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
                pstmtCliente.setString(1, cliente.getNome());
                pstmtCliente.setString(2, cliente.getCpfCnpj());
                pstmtCliente.setString(3, cliente.getEndereco());
                pstmtCliente.setString(4, cliente.getTelefone());

                int rowsAffected = pstmtCliente.executeUpdate();

                if (rowsAffected > 0) {
                    // Obtém o ID do cliente gerado
                    String idSql = "SELECT last_insert_rowid()";
                    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(idSql)) {
                        if (rs.next()) {
                            int clienteId = rs.getInt(1);
                            cliente.setId(clienteId);
                            System.out.println("Cliente salvo com ID: " + clienteId);

                            // Insira os veículos
                            try (PreparedStatement pstmtVeiculo = conn.prepareStatement(sqlVeiculo)) {
                                for (Veiculo veiculo : veiculos) {
                                    pstmtVeiculo.setInt(1, clienteId);
                                    pstmtVeiculo.setString(2, veiculo.getPlaca());
                                    pstmtVeiculo.setString(3, veiculo.getModelo());
                                    pstmtVeiculo.setLong(4, veiculo.getAno());
                                    pstmtVeiculo.setString(5, veiculo.getCor());
                                    pstmtVeiculo.addBatch(); // Adiciona ao lote
                                }
                                pstmtVeiculo.executeBatch(); // Executa o batch
                            }

                            // Commitar a transação
                            conn.commit();
                            System.out.println("Transação completa: Cliente e veículos salvos.");
                        } else {
                            conn.rollback(); // Realiza o rollback se não foi gerado o ID
                            System.out.println("Erro: ID do cliente não foi gerado!");
                        }
                    }
                } else {
                    conn.rollback(); // Rollback se não houve alterações
                    System.out.println("Erro ao salvar cliente: Nenhuma linha afetada.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Realiza rollback em caso de erro
                System.out.println("Erro ao salvar cliente e veículos: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar cliente e veículos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Atualizar um veículo
    private synchronized void atualizarVeiculo(Veiculo veiculo, Connection conn) throws SQLException {
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

        Connection conn = DatabaseConnection.connect();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
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

    public boolean deletar(int clienteId) {
        String sqlVeiculos = "DELETE FROM veiculos WHERE cliente_id = ?";
        String sqlCliente = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Iniciar transação

            // Excluir veículos associados ao cliente
            try (PreparedStatement pstmtVeiculos = conn.prepareStatement(sqlVeiculos)) {
                pstmtVeiculos.setInt(1, clienteId);
                pstmtVeiculos.executeUpdate();
            }

            // Excluir cliente
            try (PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
                pstmtCliente.setInt(1, clienteId);
                int linhasAfetadas = pstmtCliente.executeUpdate();

                if (linhasAfetadas > 0) {
                    conn.commit(); // Confirmar transação
                    System.out.println("Cliente excluído com sucesso!");
                    return true; // Cliente excluído com sucesso
                } else {
                    conn.rollback(); // Desfazer caso a exclusão falhe
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
            return false;
        }
    }

    public void atualizar(Cliente cliente, List<Veiculo> veiculos) throws SQLException {
        Connection conn = DatabaseConnection.connect();
        int tentativas = 0;
        int maxTentativas = 5;
        long intervalo = 500; // 500ms

        while (tentativas < maxTentativas) {
            try {
                conn.setAutoCommit(false);

                // Atualizar o cliente
                String sqlCliente = "UPDATE clientes SET nome = ?, cpf_cnpj = ?, endereco = ?, telefone = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlCliente)) {
                    pstmt.setString(1, cliente.getNome());
                    pstmt.setString(2, cliente.getCpfCnpj());
                    pstmt.setString(3, cliente.getEndereco());
                    pstmt.setString(4, cliente.getTelefone());
                    pstmt.setInt(5, cliente.getId());
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected == 0) {
                        System.out.println("Erro: Cliente não encontrado para atualização.");
                        conn.rollback();
                        return;
                    }
                }

                // Buscar os veículos atuais do cliente no banco
                List<Veiculo> veiculosAtuais = new VeiculoDAO().listarVeiculosPorCliente(cliente.getId());
                List<Integer> idsAtuais = veiculosAtuais.stream().map(Veiculo::getId).toList();
                List<Integer> idsNovos = veiculos.stream().map(Veiculo::getId).filter(id -> id != 0).toList();

                // Excluir veículos que não estão mais na lista
                String sqlDelete = "DELETE FROM veiculos WHERE id = ?";
                try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                    for (Veiculo veiculo : veiculosAtuais) {
                        if (!idsNovos.contains(veiculo.getId())) {
                            pstmtDelete.setInt(1, veiculo.getId());
                            pstmtDelete.executeUpdate();
                            System.out.println("Veículo com ID " + veiculo.getId() + " excluído do banco.");
                        }
                    }
                }

                // Inserir ou atualizar veículos da lista
                for (Veiculo veiculo : veiculos) {
                    if (veiculo.getId() == 0) {
                        inserirVeiculo(veiculo, cliente.getId(), conn);
                    } else if (idsAtuais.contains(veiculo.getId())) {
                        atualizarVeiculo(veiculo, conn);
                    }
                }

                conn.commit();
                System.out.println("Cliente e veículos atualizados com sucesso!");
                break;
            } catch (SQLException e) {
                conn.rollback();
                if (e.getMessage().contains("SQLITE_BUSY") && tentativas < maxTentativas - 1) {
                    tentativas++;
                    System.out.println("Banco ocupado, tentando novamente... Tentativa " + tentativas);
                    try {
                        Thread.sleep(intervalo);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.out.println("Erro ao atualizar cliente e veículos: " + e.getMessage());
                    throw e;
                }
            } finally {
                conn.close();
            }
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
    } // talvez remover isso e deixar no DAOveiculo

    public List<Cliente> buscarUltimosClientes(int limite) {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id, nome, cpf_cnpj, endereco, telefone " +
                "FROM clientes ORDER BY id DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limite);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
                cliente.setEndereco(rs.getString("endereco"));
                cliente.setTelefone(rs.getString("telefone"));

                List<Veiculo> veiculos = veiculoDAO.listarVeiculosPorCliente(cliente.getId());
                cliente.setVeiculos(veiculos);

                // Adiciona na lista
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // ou log se tiver logger
        }

        return clientes;
    }

}
