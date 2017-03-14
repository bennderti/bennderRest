/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.response;

import cl.bennder.bennderservices.model.Beneficio;
import cl.bennder.bennderservices.model.Categoria;
import cl.bennder.bennderservices.model.Validacion;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author dyanez
 */
public class CategoriaResponse extends ValidacionResponse implements Serializable{
    //private Validacion validacion;
    private List<Categoria> categoriasRelacionadas;
    private List<Beneficio> beneficios;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Validacion validacion, List<Categoria> categoriasRelacionadas, List<Beneficio> beneficios) {
        super(validacion);
        this.categoriasRelacionadas = categoriasRelacionadas;
        this.beneficios = beneficios;
    }

    public List<Categoria> getCategoriasRelacionadas() {
        return categoriasRelacionadas;
    }

    public void setCategoriasRelacionadas(List<Categoria> categoriasRelacionadas) {
        this.categoriasRelacionadas = categoriasRelacionadas;
    }

    public List<Beneficio> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(List<Beneficio> beneficios) {
        this.beneficios = beneficios;
    }
}
