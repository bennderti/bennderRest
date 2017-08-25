/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.bennderservices.services.PerfilService;
import cl.bennder.entitybennderwebrest.request.DatosPerfilRequest;
import cl.bennder.entitybennderwebrest.request.InfoDatosPerfilRequest;
import cl.bennder.entitybennderwebrest.request.InfoInicioSucursalRequest;
import cl.bennder.entitybennderwebrest.request.InfoSucursalRequest;
import cl.bennder.entitybennderwebrest.request.SucursalesRequest;
import cl.bennder.entitybennderwebrest.response.DatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.InfoDatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.InfoInicioSucursalResponse;
import cl.bennder.entitybennderwebrest.response.InfoSucursalResponse;
import cl.bennder.entitybennderwebrest.response.SucursalesResponse;
import javax.servlet.http.HttpServletRequest;
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
public class PerfilController {
    
    
    private static final Logger log = LoggerFactory.getLogger(PerfilController.class);
    
    @Autowired
    private PerfilService perfilService;   
    
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    /***
     * Obtiene los datos de perfil de usuario
     * @param request
     * @param req
     * @return 
     */
    @RequestMapping(value = "perfil/getDatos",method = RequestMethod.POST)
    public DatosPerfilResponse getDatosPerfil(@RequestBody DatosPerfilRequest request,HttpServletRequest req){
        log.info("INICIO");
        request.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(req));
        DatosPerfilResponse response = perfilService.getDatosPerfil(request);
        log.info("response ->{}",response.toString());
        log.info("FIN");
        return response;
    }
    
    /***
     * Actualiza datos de cuenta de usuario bennder
     * @param request
     * @return 
     */
    @RequestMapping(value = "perfil/guardarDatos",method = RequestMethod.POST)
    public InfoDatosPerfilResponse guardarDatosPerfil(@RequestBody InfoDatosPerfilRequest request){
        log.info("INICIO");
        InfoDatosPerfilResponse response = perfilService.guardarDatosPerfil(request);
        log.info("response ->{}",response.toString());
        log.info("FIN");
        return response;
    }
    
}
