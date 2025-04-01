package com.example.reporteurbano.model;

public class LoginRequest { //uma classe responsável por armazenas as informações deusuário e senha

    //não chamamos diretamente a UsuarioModel para não ter um número alto de processamentos
    //e não ter o perigo de chamar uma classe sensível antes do necessário
    private String cpf;
    private String nome;

    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
