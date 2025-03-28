package com.example.testechatgemini.model;

import jakarta.persistence.*;

@Entity
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int idUsuario;
    private String tituloProblema;
    private String descricao;
    private String localizacao;
    private String foto;

    public Ocorrencia() {}

    public Ocorrencia(int idUsuario, String tituloProblema, String descricao, String localizacao, String foto) {
        this.idUsuario = idUsuario;
        this.tituloProblema = tituloProblema;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.foto = foto;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getTituloProblema() { return tituloProblema; }
    public void setTituloProblema(String tituloProblema) { this.tituloProblema = tituloProblema; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
