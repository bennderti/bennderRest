/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.response;

import cl.bennder.bennderservices.model.Beneficio;
import cl.bennder.bennderservices.model.Validacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Diego
 */
public class BeneficiosResponse implements Serializable{
    private Validacion validacion;
    private List<Beneficio> beneficios;

    public BeneficiosResponse() {
    }

    public BeneficiosResponse(Validacion validacion, List<Beneficio> beneficios) {
        this.validacion = validacion;
        this.beneficios = beneficios;
    }

    public Validacion getValidacion() {
        if(validacion == null){
            validacion = new Validacion();
        }
        return validacion;
    }

    public void setValidacion(Validacion validacion) {
        this.validacion = validacion;
    }

    public List<Beneficio> getBeneficios() {
        if(beneficios == null){
            this.beneficios = new ArrayList<>();
        }
        return beneficios;
    }

    public void setBeneficios(List<Beneficio> beneficios) {
        this.beneficios = beneficios;
    }

    @Override
    public String toString() {
        return "BeneficiosResponse{" + "validacion=" + validacion + ", beneficios=" + beneficios + '}';
    }
    
}
