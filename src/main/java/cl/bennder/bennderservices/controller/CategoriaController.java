/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.constantes.CodigoValidacion;
import cl.bennder.bennderservices.model.Categoria;
import cl.bennder.bennderservices.model.Validacion;
import cl.bennder.bennderservices.request.BeneficiosRequest;
import cl.bennder.bennderservices.request.CategoriasRequest;
import cl.bennder.bennderservices.response.BeneficiosResponse;
import cl.bennder.bennderservices.response.CategoriaResponse;
import cl.bennder.bennderservices.response.CategoriasResponse;
import cl.bennder.bennderservices.services.CategoriaServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author dyanez
 */
@RestController
public class CategoriaController {
    
    private static final Logger log = LoggerFactory.getLogger(CategoriaController.class);
    
    @Autowired
    private CategoriaServices categoriaServices;
    
    //.- login
    @RequestMapping(value = "getCategorias",method = RequestMethod.POST)
    public CategoriasResponse getCategorias(@RequestBody CategoriasRequest request){
        log.info("INICIO");
        CategoriasResponse response = categoriaServices.getCategorias(request);
        log.info("response ->{}",response.toString());
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "obtenerBeneficiosPorCategoria",method = RequestMethod.POST)
    public BeneficiosResponse obtenerBeneficiosPorCategoria(@RequestBody BeneficiosRequest request){
        log.info("INICIO");
        BeneficiosResponse response = null;
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "obtenerCategoriasRelacionadas",method = RequestMethod.POST)
    public CategoriasResponse obtenerCategoriasRelacionadas(@RequestBody CategoriasRequest request){
        log.info("INICIO");
        CategoriasResponse response = categoriaServices.obtenerCategoriasRelacionadas(request);
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "cargarCategoria",method = RequestMethod.POST)
    public CategoriaResponse cargarCategoria(@RequestBody CategoriasRequest request){
        log.info("INICIO");
        CategoriaResponse response = categoriaServices.cargarCategoria(request);
        log.info("FIN");
        return response;
    }
}
