/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.CategoriaByIdRequest;
import cl.bennder.entitybennderwebrest.request.CategoriasRequest;
import cl.bennder.entitybennderwebrest.response.BeneficiosResponse;
import cl.bennder.entitybennderwebrest.response.CategoriaResponse;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;


/**
 *
 * @author dyanez
 */
public interface CategoriaServices {
    CategoriasResponse obtenerCategoriasById(CategoriaByIdRequest request);
    BeneficiosResponse getBeneficiosByIdCat(CategoriaByIdRequest request); 
    CategoriasResponse getCategorias(CategoriasRequest request);
    CategoriasResponse obtenerCategoriasRelacionadas(CategoriasRequest request);
    CategoriaResponse cargarCategoria(CategoriasRequest request);
}
