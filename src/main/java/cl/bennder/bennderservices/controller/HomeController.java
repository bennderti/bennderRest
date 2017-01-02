/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.model.Validacion;
import cl.bennder.bennderservices.request.LoginRequest;
import cl.bennder.bennderservices.response.LoginResponse;
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
public class HomeController {
    
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private UsuarioServices usuarioServices;
    
    //.- login
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public @ResponseBody LoginResponse login(@RequestBody LoginRequest request){
        log.info("[login] - inicio ");        
        LoginResponse response = new LoginResponse(new Validacion("0", "Welcome "+request.getUser()+" to Bennder!"));
        usuarioServices.listarUsuarios();
        log.info("response ->{}",response.toString());
        log.info("[login] - fin ");
        return response;
    }
}
