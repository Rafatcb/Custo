package br.unicamp.ft.r176257.myapplication;


public class Idioma {
    String idioma;
    int resId;

    public Idioma(String idioma, int resId){
        this.idioma = idioma;
        this.resId = resId;

    }
    public void setIdioma(String idioma){
        this.idioma = idioma;
    }
    public String getIdioma(){
        return idioma;
    }
}
