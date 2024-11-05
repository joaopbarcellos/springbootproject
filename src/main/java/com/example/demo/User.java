package com.example.demo;

public class User {
    protected String login, nome, senha;
    protected Long id;

    public Long getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public String getLogin(){
        return login;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public void setId(Long id){
        this.id = id;
    }
}
