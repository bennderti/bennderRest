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
public class RecuperacionPasswordRequest implements Serializable{
    private String usuarioCorreo;// correo de destino al que se envia la contraseña

    public RecuperacionPasswordRequest(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    public RecuperacionPasswordRequest() {
    }

    public String getUsuarioCorreo() {
        return usuarioCorreo;
    }

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    @Override
    public String toString() {
        return "RecuperacionPasswordRequest{" + "usuarioCorreo=" + usuarioCorreo + '}';
    }
    
    
    
}
