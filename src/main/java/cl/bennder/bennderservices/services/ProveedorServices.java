/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.DatosGeneralProveedorRequest;
import cl.bennder.entitybennderwebrest.request.ProveedorIdRequest;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import cl.bennder.entitybennderwebrest.response.DatosGeneralProveedorResponse;
import cl.bennder.entitybennderwebrest.response.ProveedoresResponse;

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
    
    /***
     * Servicio que permite guardar/actualizar los datos generales de proveedor
     * @param request Datos generales de proveedor
     * @return Validación de guardado de proveedor
     */
    public DatosGeneralProveedorResponse guardaDatosGeneralesProveedor(DatosGeneralProveedorRequest request);
    
    /***
     * Método encargado de generar y guardar logo de imagen de proveedor en sistema de archivos de servidor
     * DY - 19.04.2017 
     * @param imagen Logo de proveedor
     * @param idProveedor Identificador de proveedor
     * @param extension Extensión del logo
     * @return Ruta de logo en sistema de archivos
     */
    public String guardaLogoImagenSistemaArchivos(byte[] imagen, Integer idProveedor,  String extension);
}
