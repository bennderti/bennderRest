/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.model;

/**
 *
 * @author Marcos
 */
public class Contacto {
    
    private Integer idContacto;
    private Integer celular;
    private Integer fonoFijo;
    private String correo;
    
    public Contacto(){
        
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    public void setFonoFijo(Integer fonoFijo) {
        this.fonoFijo = fonoFijo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }    
}
