package br.unicamp.ft.r176257.myapplication;

import java.util.Date;

public class Despesa {
    private int id;
    private Categoria categoria;
    private float despesa;
    private Date data;

    public void setCategoria(Categoria c) {
        categoria = c;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setDespesa(float d) {
        despesa = d;
    }

    public float getDespesa() {
        return despesa;
    }

    public void setData(Date d) {
        data = d;
    }

    public Date getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
