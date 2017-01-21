/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.model.Validacion;
import cl.bennder.bennderservices.request.CategoriasRequest;
import cl.bennder.bennderservices.request.LoginRequest;
import cl.bennder.bennderservices.response.CategoriasResponse;
import cl.bennder.bennderservices.response.LoginResponse;
import cl.bennder.bennderservices.services.CategoriaServices;
import cl.bennder.bennderservices.services.UsuarioServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public @ResponseBody CategoriasResponse getCategorias(@RequestBody CategoriasRequest request){
        log.info("[getCategorias] - inicio ");        
        CategoriasResponse response = categoriaServices.getCategorias(request);
        //log.info("response ->{}",response.toString());
        log.info("[getCategorias] - fin ");
        return response;
    }
}
