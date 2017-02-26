/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.CodigoValidacion;
import cl.bennder.bennderservices.mapper.UsuarioMapper;
import cl.bennder.bennderservices.model.Validacion;
import cl.bennder.bennderservices.request.LoginRequest;
import cl.bennder.bennderservices.response.LoginResponse;
import java.util.List;

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
public class UsuarioServicesImpl implements UsuarioServices{
    
    private static final Logger log = LoggerFactory.getLogger(UsuarioServicesImpl.class);
    
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    public void listarUsuarios() {
        log.info("INICIO");
        List<Integer> users = usuarioMapper.getUsers();
        if(users!= null){
            for(Integer user:users){
                log.info("usuario ->{}",user);
            }
        }
        log.info("FIN");
    }

    @Override
    public LoginResponse validacionUsuario(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO, "Problema en validación de usuario"));
        log.info("INICIO");
        Integer validacion = 0;
        try {
            if(request.getUser()!=null && request.getPassword()!=null){
                log.info("Validando usuario ->{}",request.getUser());
                //usuario existe?
                validacion = usuarioMapper.validaUsuario(request.getUser(), request.getPassword());
                if(validacion!=null && validacion.compareTo(1) == 0){
                    //.- obtener idUSuario(rut usuario sin dv) por usuario
                    response.setIdUsuario(usuarioMapper.getIdUsuario(request.getUser()));                    
                    response.getValidacion().setCodigo(CodigoValidacion.OK);
                    response.getValidacion().setMensaje("Validación OK");
                    log.info("Validación OK");
                }
                else{
                    response.getValidacion().setMensaje("Usuario y contraseña incorrectos");
                    log.info("Usuario y contraseña incorrectos");
                }
            }
            else{
                log.info("Favor completar datos para validación de usuario");
                response.getValidacion().setMensaje("Favor completar datos para validación de usuario");
            }

        } catch (Exception e) {
            log.error("Error en Exception:",e);
            response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO, "Error en validación de usuario"));
            
        }
        log.info("FIN");
        return response;
    }
    
}
