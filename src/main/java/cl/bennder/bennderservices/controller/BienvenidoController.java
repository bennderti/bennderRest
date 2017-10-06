/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.bennderservices.services.BienvenidoServices;
import cl.bennder.bennderservices.services.UsuarioServices;
import cl.bennder.entitybennderwebrest.request.BienvenidoRequest;
import cl.bennder.entitybennderwebrest.request.GuardarDatosBienvenidaRequest;
import cl.bennder.entitybennderwebrest.response.BienvenidoResponse;
import cl.bennder.entitybennderwebrest.response.GuardarDatosBienvenidaResponse;
import javax.servlet.http.HttpServletRequest;
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
    
    @Autowired
    private BienvenidoServices bienvenidoServices;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
     
    @RequestMapping(value = "bienvenido/obtenerDatosBienvenida",method = RequestMethod.POST)
    public @ResponseBody BienvenidoResponse obtenerDatosBienvenida(@RequestBody BienvenidoRequest request){
        log.info("INICIO");
        BienvenidoResponse response = bienvenidoServices.obtenerDatosBienvenida(request);
        log.info("FIN");
        return response;
    }    
         
    @RequestMapping(value = "bienvenido/guardarDatosBienvenida", method = RequestMethod.POST)
    public @ResponseBody GuardarDatosBienvenidaResponse guardarDatosBienvenida (@RequestBody GuardarDatosBienvenidaRequest request, HttpServletRequest hsRequest) {
        log.info("INICIO");
        request.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(hsRequest));
        
        GuardarDatosBienvenidaResponse response = bienvenidoServices.guardarDatosBienvenida(request);
                
        log.info("response ->{}", response.getValidacion().getMensaje());
        log.info("FIN");
        
        return response;
    } 
}
