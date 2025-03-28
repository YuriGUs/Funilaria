package dev.yuri.model;

public class ItemOrcamento {
    private int id;
    private int idOrcamento;
    private int quantidade;
    private String descricao;
    private double valorUnitario;
    private double valorTotal;
    private String responsavel;

    // Construtor completo
    public ItemOrcamento(int idOrcamento, int quantidade, String descricao,
                         double valorUnitario, String responsavel) {
        this.idOrcamento = idOrcamento;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.valorTotal = quantidade * valorUnitario;
        this.responsavel = responsavel;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdOrcamento() { return idOrcamento; }
    public void setIdOrcamento(int idOrcamento) { this.idOrcamento = idOrcamento; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.valorTotal = quantidade * valorUnitario;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
        this.valorTotal = quantidade * valorUnitario;
    }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}