/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.ProveedorMapper;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.ProveedorIdRequest;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import cl.bennder.entitybennderwebrest.response.ProveedoresResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dyanez
 */
@Service
@Transactional
public class ProveedorServicesImpl implements ProveedorServices{
    
    private static final Logger log = LoggerFactory.getLogger(ProveedorServicesImpl.class);
    
    @Autowired
    private ProveedorMapper proveedorMapper;

    @Override
    public CategoriasResponse obtenerCategoriaByProveedor(ProveedorIdRequest request) {
        CategoriasResponse response = new CategoriasResponse();
        response.setValidacion(new Validacion("0", "1", "Sin categorias para proveedor"));
        
        log.info("inicio");
        try {
            log.info("obtienendo categorias para proveedor ->{}",request.getIdProveedor());
            response.setCategorias(proveedorMapper.obtenerCategoriaProveedor(request.getIdProveedor()));
            response.getValidacion().setCodigoNegocio("0");
            response.getValidacion().setMensaje("Categorias ok para proveedor");
        } catch (Exception e) {
            log.error("Error en obtenerCategoriaByProveedor",e);
            response.setValidacion(new Validacion("1", "1", "Error al obtener categorias para proveedor"));
        }
        log.info("fin");
        
        return response;
    }

    @Override
    public ProveedoresResponse obtenerProveedorHabilitados(ProveedorIdRequest request) {
         
        ProveedoresResponse response = new ProveedoresResponse();
        response.setValidacion(new Validacion("0", "1", "Sin categorias para proveedor"));
        
        log.info("inicio");
        try {
            response.setProveedores(proveedorMapper.obtenerProveedorHabilitados());
            response.getValidacion().setCodigoNegocio("0");
            response.getValidacion().setMensaje("Proveedores OK");
        } catch (Exception e) {
            log.error("Error en obtenerProveedorHabilitados",e);
            response.setValidacion(new Validacion("1", "1", "Error al obtener proveedores habilitados"));
        }
        log.info("fin");
        
        return response;
    }
    
    
    
    
    
}
