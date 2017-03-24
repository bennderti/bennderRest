/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.model.Proveedor;
import cl.bennder.entitybennderwebrest.request.ProveedorIdRequest;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import cl.bennder.entitybennderwebrest.response.ProveedoresResponse;
import java.util.List;

/**
 *
 * @author dyanez
 */
public interface ProveedorServices {
    
    /***
     * Obtiene la lista de categorias de un proveedor
     * @author dyanez
     * @param request Identificar de proveedor
     * @return Lista de categorias para proveedor
     */
    public CategoriasResponse obtenerCategoriaByProveedor(ProveedorIdRequest request);
    
    /***
     * Proveedores habilitados
     * @param request
     * @return 
     */
    public ProveedoresResponse obtenerProveedorHabilitados(ProveedorIdRequest request);
}
