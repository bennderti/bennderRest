/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.request.CategoriasRequest;
import cl.bennder.bennderservices.response.CategoriasResponse;

/**
 *
 * @author dyanez
 */
public interface CategoriaServices {
    CategoriasResponse getCategorias(CategoriasRequest request);
    CategoriasResponse obtenerCategoriasRelacionadas(CategoriasRequest request);
}
