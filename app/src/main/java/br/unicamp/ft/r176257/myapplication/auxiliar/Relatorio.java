package br.unicamp.ft.r176257.myapplication.auxiliar;

public class Relatorio {
    private int relatorio;
    private String categoria;
    private float despesa;
    private String dataInicio;
    private String dataFim;

    public Relatorio() {

    }

    public Relatorio(int relatorio, String categoria, float despesa, String dataInicio, String dataFim) {
        this.relatorio = relatorio;
        this.categoria = categoria;
        this.despesa = despesa;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(int relatorio) {
        this.relatorio = relatorio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getDespesa() {
        return despesa;
    }

    public void setDespesa(float despesa) {
        this.despesa = despesa;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
