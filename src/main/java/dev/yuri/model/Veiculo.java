package dev.yuri.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Veiculo {
    private final IntegerProperty id;
    private final StringProperty placa;
    private final StringProperty modelo;
    private final IntegerProperty ano;
    private final StringProperty cor;
    private final IntegerProperty clienteId;

    // Construtor com 5 parâmetros
    public Veiculo(int id, String placa, String modelo, int ano, String cor, int clienteId) {
        this.id = new SimpleIntegerProperty(id);
        this.placa = new SimpleStringProperty(placa);
        this.modelo = new SimpleStringProperty(modelo);
        this.ano = new SimpleIntegerProperty(ano);
        this.cor = new SimpleStringProperty(cor);
        this.clienteId = new SimpleIntegerProperty(clienteId);
    }

    // Construtor alternativo (com 4 parâmetros)
    public Veiculo(String placa, String modelo, int ano, String cor) {
        this.id = new SimpleIntegerProperty(0); // id default como 0, pois pode ser gerado automaticamente
        this.placa = new SimpleStringProperty(placa);
        this.modelo = new SimpleStringProperty(modelo);
        this.ano = new SimpleIntegerProperty(ano);
        this.cor = new SimpleStringProperty(cor);
        this.clienteId = new SimpleIntegerProperty(0); // clienteId default como 0
    }

    // Getters para JavaFX Property
    public IntegerProperty idProperty() { return id; }
    public StringProperty placaProperty() { return placa; }
    public StringProperty modeloProperty() { return modelo; }
    public IntegerProperty anoProperty() { return ano; }
    public StringProperty corProperty() { return cor; }
    public IntegerProperty clienteIdProperty() { return clienteId; }

    // Getters normais
    public int getId() { return id.get(); }
    public String getPlaca() { return placa.get(); }
    public String getModelo() { return modelo.get(); }
    public int getAno() { return ano.get(); }
    public String getCor() { return cor.get(); }
    public int getClienteId() { return clienteId.get(); }

    // Setters normais
    public void setId(int id) { this.id.set(id); }
    public void setPlaca(String placa) { this.placa.set(placa); }
    public void setModelo(String modelo) { this.modelo.set(modelo); }
    public void setAno(int ano) { this.ano.set(ano); }
    public void setCor(String cor) { this.cor.set(cor); }
    public void setClienteId(int clienteId) { this.clienteId.set(clienteId); }
}
