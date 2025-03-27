package com.example.testechatgemini;

public class Problema {
    private int id;
    private int id_usuario;
    private String tituloProblema;
    private String descricao;
    private String localizacao;
    private String foto;

    public Problema(int id, int id_usuario, String tituloProblema, String descricao, String localizacao, String foto) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.tituloProblema = tituloProblema;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTituloProblema() {
        return tituloProblema;
    }

    public void setTituloProblema(String tituloProblema) {
        this.tituloProblema = tituloProblema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String obterResposta(){
        return "";
    }
}
