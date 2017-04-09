/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.services.UsuarioServices;
import cl.bennder.entitybennderwebrest.request.GuardarPreferenciasRequest;
import cl.bennder.entitybennderwebrest.request.LoginRequest;
import cl.bennder.entitybennderwebrest.response.LoginResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;
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
 * @author Marcos
 */

@RestController
public class BienvenidoController {
    
    private static final Logger log = LoggerFactory.getLogger(BienvenidoController.class);
    
    @Autowired
    private UsuarioServices usuarioServices;
     
    @RequestMapping(value = "bienvenido/guadarPreferencias", method = RequestMethod.POST)
    public @ResponseBody
    ValidacionResponse guardarPreferencias(@RequestBody GuardarPreferenciasRequest request) {
        log.info("[login] - inicio ");
        ValidacionResponse response = usuarioServices.guardarDatosBienvenidaUsuario(request);
        log.info("response ->{}", response.toString());
        log.info("[login] - fin ");
        
        return response;
    } 
}
