package dev.yuri.model;

import javafx.beans.property.*;
import java.util.List;

public class Cliente {

    private IntegerProperty id;
    private StringProperty nome;
    private StringProperty cpfCnpj;
    private StringProperty endereco;
    private StringProperty telefone;
    private List<Veiculo> veiculos; // Relação 1:N

    private void inicializarPropriedades() {
        this.id = new SimpleIntegerProperty();
        this.nome = new SimpleStringProperty();
        this.cpfCnpj = new SimpleStringProperty();
        this.endereco = new SimpleStringProperty();
        this.telefone = new SimpleStringProperty();
    }

    public Cliente() {
        inicializarPropriedades();
    }

    public Cliente(int id, String nome, String cpfCnpj, String endereco, String telefone) {
        inicializarPropriedades();
        this.id.set(id);
        this.nome.set(nome);
        this.cpfCnpj.set(cpfCnpj);
        this.endereco.set(endereco);
        this.telefone.set(telefone);
    }

    public Cliente(String nome, String cpfCnpj, String endereco, String telefone) {
        inicializarPropriedades();
        this.nome.set(nome);
        this.cpfCnpj.set(cpfCnpj);
        this.endereco.set(endereco);
        this.telefone.set(telefone);
        this.id.set(-1);
    }



    // Getters e setters para todos os campos
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj.get();
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj.set(cpfCnpj);
    }

    public StringProperty cpfCnpjProperty() {
        return cpfCnpj;
    }

    public String getEndereco() {
        return endereco.get();
    }

    public void setEndereco(String endereco) {
        this.endereco.set(endereco);
    }

    public StringProperty enderecoProperty() {
        return endereco;
    }

    public String getTelefone() {
        return telefone.get();
    }

    public void setTelefone(String telefone) {
        this.telefone.set(telefone);
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }
}
