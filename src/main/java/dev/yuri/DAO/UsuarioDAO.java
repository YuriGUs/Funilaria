package dev.yuri.DAO;

import dev.yuri.util.DatabaseConnection;
import dev.yuri.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public String hashedPassword(String password) {
        try {
            System.out.println("Gerando hash para: '" + password + "'");
            byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
            System.out.println("Bytes da string: " + java.util.Arrays.toString(bytes));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(bytes);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String result = hexString.toString();
            System.out.println("Hash gerado: " + result);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criar hash: " + e.getMessage());
        }
    }

    public void salvar(Usuario usuario) {
        String sqlUsuario = "INSERT INTO usuarios (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect()){
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUsuario)) {
                pstmt.setString(1, usuario.getUsername());
                pstmt.setString(2, hashedPassword(usuario.getPassword())); // salvando no banco com o hash
                pstmt.setString(3, usuario.getRole());
                pstmt.executeUpdate();

                conn.commit();
                System.out.println("Usuário salvo com sucesso!");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("ERRO ao salvar usuario" + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao se conectar para salvar usuario" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Usuario autenticar(String username, String password) {
        String sqlBuscaUser = "SELECT * FROM usuarios WHERE username = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuscaUser)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String inputHash = hashedPassword(password);
                    System.out.println("Stored Hash: " + storedHash);
                    System.out.println("Input Hash: " + inputHash);

                    if (storedHash.equals(inputHash)) {
                        return new Usuario(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("role")
                        );
                    } else {
                        System.out.println("Hashes não coincidem!");
                    }
                } else {
                    System.out.println("Usuário não encontrado: " + username);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao autenticar usuário: " + e.getMessage());
        }
        return null;
    }

}
