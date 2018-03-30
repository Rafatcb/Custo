package br.unicamp.ft.r176257.myapplication;

import java.util.Date;

public class Despesa {
    private String categoria;
    private float despesa;
    private Date data;

    public void setCategoria(String c) {
        categoria = c;
    }

    public String getCategoria() {
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
}
