package br.unicamp.ft.r176257.myapplication.auxiliar;

public class Categoria {
    private int id;
    private String nome;
    private String cor; // Em hex

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
