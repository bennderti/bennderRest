/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.response;

import cl.bennder.bennderservices.model.Validacion;

/**
 *
 * @author dyanez
 */
public class LoginResponse {
    private Validacion validacion;

    public LoginResponse(Validacion validacion) {
        this.validacion = validacion;
    }
    
    public LoginResponse() {
    }
    

    public Validacion getValidacion() {
        return validacion;
    }

    public void setValidacion(Validacion validacion) {
        this.validacion = validacion;
    }

    @Override
    public String toString() {
        return "LoginResponse{" + "validacion=" + validacion + '}';
    }
    
}
