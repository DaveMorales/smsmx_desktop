/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minimalsoft.smsmx.desktop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author DaveMorales
 */
public class SendSmsRequest {

    @SerializedName("message")
    @Expose
    private String message;
    
    @SerializedName("numbers")
    @Expose
    private List<String> numbers = null;
    
    @SerializedName("client")
    @Expose
    private String client;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public SendSmsRequest(String message, String client, List<String> numbers) {
        this.message = message;
        this.numbers = numbers;
        this.client = client;
    }
    
    
}
