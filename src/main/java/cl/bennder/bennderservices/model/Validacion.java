/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.model;

import java.io.Serializable;

/**
 *
 * @author dyanez
 */
public class Validacion implements Serializable{
    private String codigo;//0: Indica que servicio responde OK,1: Indica servicio responde NOK
    private String codigoNegocio;//0: Indica exito, y otro distino es para comportamiento a futuro
    private String mensaje;
    public Validacion() {
        this.codigo = "1";
        this.codigoNegocio = "0";
        this.mensaje = "Error en validación";
    }
    
    public Validacion(String codigo, String codigoNegocio, String mensaje) {
        this.codigo = codigo;
        this.codigoNegocio = codigoNegocio;
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoNegocio() {
        return codigoNegocio;
    }

    public void setCodigoNegocio(String codigoNegocio) {
        this.codigoNegocio = codigoNegocio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
}
