package br.unicamp.ft.r176257.myapplication.auxiliar;


public class Idioma {
    private String locale1;
    private String locale2;
    private String idioma;
    private int resId;

    public Idioma(String idioma, String loc1, int resId){
        this.idioma = idioma;
        this.locale1 = loc1;
        this.locale2 = "";
        this.resId = resId;

    }
    public void setIdioma(String idioma){
        this.idioma = idioma;
    }
    public String getIdioma(){
        return idioma;
    }
    public void setLocale1(String locale1){
        this.locale1 = locale1;
    }
    public String getLocale1(){
        return locale1;
    }
    public void setLocale2 (String locale2) { this.locale2 = locale2; }
    public String getLocale2(){
        return locale2;
    }
    public int getResId() {
        return resId;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean hasLocale2() {
        return !(locale2.equals(""));
    }
}
