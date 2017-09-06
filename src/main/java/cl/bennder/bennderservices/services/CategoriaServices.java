/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.model.Categoria;
import cl.bennder.entitybennderwebrest.request.*;
import cl.bennder.entitybennderwebrest.response.*;

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
    /***
     * Servicio que obtiene una lista de categorias (sub-categorias) validas del proveedor, es decir, en las cuales tiene beneficio
     * @param request
     * @return 
     */
    SubCategoriaProveedorResponse getSubCategoriasProveedor(SubCategoriaProveedorRequest request);
    BeneficiosResponse filtrarBeneficiosCategoriaPorPrecio(FiltrarBeneficiosRangoRequest request);
    BeneficiosResponse filtrarBeneficiosCategoriaPorDescuento(FiltrarBeneficiosRangoRequest request);
    BeneficiosResponse filtrarBeneficiosPorProveedor(FiltrarBeneficiosRequest request);
    BeneficiosResponse filtrarBeneficiosPorCalificacion(FiltrarBeneficiosRequest request);
}
