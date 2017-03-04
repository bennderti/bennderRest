/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.response;

import cl.bennder.bennderservices.model.Validacion;
import java.io.Serializable;

/**
 *
 * @author dyanez
 */
public class LoginResponse extends ValidacionResponse implements Serializable{
    private Integer idUsuario;//rut sin dv

    public LoginResponse(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LoginResponse(Integer idUsuario, Validacion validacion) {
        super(validacion);
        this.idUsuario = idUsuario;
    }

    public LoginResponse() {
    }
    
    //se dejó de esta manera, como una de las mejores prácticas y así instanciar sólo cuando se utilice
//    public Validacion getValidacion() {
//        if(validacion == null){
//            validacion = new Validacion();
//        }
//        return validacion;
//    }
//
//    public void setValidacion(Validacion validacion) {
//        this.validacion = validacion;
//    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "LoginResponse{" + "idUsuario=" + idUsuario + '}';
    }

    
    
}
