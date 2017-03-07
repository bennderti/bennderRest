/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author dyanez
 */

public class Categoria implements Serializable{
    private Integer idCategoria;
    private String nombre;
    private Integer idCategoriaPadre;
    private List<Categoria> subCategorias;

    public Categoria() {
    }

    public Categoria(Integer idCategoria, String nombre, Integer idCategoriaPadre, List<Categoria> subCategorias) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.subCategorias = subCategorias;
        this.idCategoriaPadre = idCategoriaPadre;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Categoria> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<Categoria> subCategorias) {
        this.subCategorias = subCategorias;
    }

    public Integer getIdCategoriaPadre() {
        return idCategoriaPadre;
    }

    public void setIdCategoriaPadre(Integer idCategoriaPadre) {
        this.idCategoriaPadre = idCategoriaPadre;
    }

    @Override
    public String toString() {
        return "Categoria{" + "idCategoria=" + idCategoria + ", nombre=" + nombre + '}';
    }
    
}
