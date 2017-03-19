/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.request.LoginRequest;
import cl.bennder.bennderservices.request.RecuperacionPasswordRequest;
import cl.bennder.bennderservices.response.LoginResponse;
import cl.bennder.bennderservices.response.ValidacionResponse;
import cl.bennder.bennderservices.services.UsuarioServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cl.bennder.bennderservices.services.EmailServices;

/**
 *
 * @author dyanez
 */
@RestController
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UsuarioServices usuarioServices;

    @Autowired
    private EmailServices emailServices;

    //.- login
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public @ResponseBody
    LoginResponse login(@RequestBody LoginRequest request) {
        log.info("[login] - inicio ");
        LoginResponse response = usuarioServices.validacionUsuario(request);
        log.info("response ->{}", response.toString());
        log.info("[login] - fin ");
        
        return response;
    }
    /**
     * Utilizado para testear email utilizando velocity como template
     * @param  to Destinatario 
     */
    @RequestMapping(value = "velocity/emailMimeMessage", method = RequestMethod.POST)
    public void velocityEmailMimeMessage(@RequestParam("to") String to) {
        log.info("[velocity] - inicio ");
        emailServices.sendEmailMimeMessage(to);
        //emailByVelocity.sendEmail(to);
        log.info("[velocity] - fin ");
    }
     /***
     * Servicio utilizado para enviar contraseña a correo de usuario
     * @param  request Usuario correo destinatario 
     * @return 
     */
    @RequestMapping(value = "mail/recuperacionPassword", method = RequestMethod.POST)
    public ValidacionResponse recuperacionPassword(@RequestBody RecuperacionPasswordRequest request) {
        log.info("[mail/recuperacionPassword] - inicio ");
        ValidacionResponse response = emailServices.recuperacionPassword(request);
        log.info("[mail/recuperacionPassword] - fin ");
        return response;
    }
}
