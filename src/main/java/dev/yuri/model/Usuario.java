package dev.yuri.model;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String role;

    public Usuario(int id, String username, String password, String role ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
