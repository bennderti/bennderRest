/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.model.Categoria;
import cl.bennder.entitybennderwebrest.request.CategoriaByIdRequest;
import cl.bennder.entitybennderwebrest.request.CategoriasRequest;
import cl.bennder.entitybennderwebrest.response.BeneficiosCargadorResponse;
import cl.bennder.entitybennderwebrest.response.CategoriaResponse;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import java.util.List;


/**
 *
 * @author dyanez
 */
public interface CategoriaServices {
    CategoriasResponse obtenerCategoriasById(CategoriaByIdRequest request);
    BeneficiosCargadorResponse getBeneficiosByIdCat(CategoriaByIdRequest request); 
    CategoriasResponse getCategorias(CategoriasRequest request);
    List<Categoria> getCategorias();
    CategoriasResponse obtenerCategoriasRelacionadas(CategoriasRequest request);
    CategoriaResponse cargarCategoria(CategoriasRequest request);
}
