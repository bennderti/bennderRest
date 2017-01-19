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
public class Categoria implements Serializable{
    private Integer idCategoria;
    private String nomnbre;

    public Categoria() {
    }

    public Categoria(Integer idCategoria, String nomnbre) {
        this.idCategoria = idCategoria;
        this.nomnbre = nomnbre;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomnbre() {
        return nomnbre;
    }

    public void setNomnbre(String nomnbre) {
        this.nomnbre = nomnbre;
    }

    @Override
    public String toString() {
        return "Categoria{" + "idCategoria=" + idCategoria + ", nomnbre=" + nomnbre + '}';
    }
    
}
