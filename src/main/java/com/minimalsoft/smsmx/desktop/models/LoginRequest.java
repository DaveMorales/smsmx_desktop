/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minimalsoft.smsmx.desktop.models;

/**
 *
 * @author DaveMorales
 */
public class LoginRequest {

    private String user;

    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequest(String user, String password) {
        this.user = user;
        this.password = password;
    }

}
