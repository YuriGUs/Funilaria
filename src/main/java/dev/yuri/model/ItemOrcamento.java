package dev.yuri.model;

public class ItemOrcamento {
    private int quantidade;
    private String descricao;
    private double valorUnitario;
    private double valorTotal;
    private String responsavel;

    public ItemOrcamento(int quantidade, String descricao, double valorUnitario, String responsavel) {
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.valorTotal = quantidade * valorUnitario;
        this.responsavel = responsavel;
    }

    // Getters e Setters
    public int getQuantidade() { return quantidade; }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.valorTotal = quantidade * valorUnitario; // Atualiza o total
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorUnitario() { return valorUnitario; }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
        this.valorTotal = quantidade * valorUnitario; // Atualiza o total
    }

    public double getValorTotal() { return valorTotal; }

    public String getResponsavel() { return responsavel; }

    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}
