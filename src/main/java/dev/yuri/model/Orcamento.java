package dev.yuri.model;

import java.util.Date;

public class Orcamento {
    private int id;
    private int idCliente;
    private int idVeiculo;
    private Date data;  // Usando java.util.Date
    private double valorTotal;

    public Orcamento(int idCliente, int idVeiculo, Date data, double valorTotal) {
        this.idCliente = idCliente;
        this.idVeiculo = idVeiculo;
        this.data = data != null ? data : new Date();
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getDataFormatada() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(data);
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}