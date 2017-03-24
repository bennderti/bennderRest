/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.services.ProveedorServices;
import cl.bennder.entitybennderwebrest.request.CategoriaByIdRequest;
import cl.bennder.entitybennderwebrest.request.ProveedorIdRequest;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import cl.bennder.entitybennderwebrest.response.ProveedoresResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dyanez
 */
@RestController
public class ProveedorController {
    
    
    private static final Logger log = LoggerFactory.getLogger(ProveedorController.class);
    
    @Autowired
    private ProveedorServices proveedorServices;    
    
    @RequestMapping(value = "obtenerCategoriaByProveedor",method = RequestMethod.POST)
    public CategoriasResponse obtenerCategoriasById(@RequestBody ProveedorIdRequest request){
        log.info("INICIO");
        CategoriasResponse response = proveedorServices.obtenerCategoriaByProveedor(request);
        log.info("FIN");
        return response;
    }
    
    @RequestMapping(value = "obtenerProveedorHabilitados",method = RequestMethod.POST)
    public ProveedoresResponse obtenerProveedorHabilitados(@RequestBody ProveedorIdRequest request){
        log.info("INICIO");
        ProveedoresResponse response = proveedorServices.obtenerProveedorHabilitados(request);
        log.info("FIN");
        return response;
    }
}
