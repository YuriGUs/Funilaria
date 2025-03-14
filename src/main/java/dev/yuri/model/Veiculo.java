package dev.yuri.model;

public class Veiculo {
    private int id;
    private int clienteId;
    private String placa;
    private String modelo;
    private int ano;
    private String cor;

    public Veiculo(int id, int clienteId, String placa, String modelo, int ano, String cor) {
        this.id = id;
        this.clienteId = clienteId;
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
