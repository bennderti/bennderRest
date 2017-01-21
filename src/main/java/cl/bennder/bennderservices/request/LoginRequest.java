/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.request;

import java.io.Serializable;

/**
 *
 * @author dyanez
 */
public class LoginRequest implements Serializable{
    private Integer user;
    private String password;

    public LoginRequest() {
    }
     public LoginRequest(Integer user, String password) {
        this.user = user;
        this.password = password;
    }
    
    
    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
     
}
