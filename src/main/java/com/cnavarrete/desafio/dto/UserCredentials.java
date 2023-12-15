package com.cnavarrete.desafio.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserCredentials {

    public UserCredentials(String mail, String pass, String token) {
        this.mail = mail;
        this.pass = pass;
        this.token = token;
    }


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String mail;


    private String pass;

    private String token;
}
