/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.UsuarioMapper;
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
    
//    @Autowired
//    private UsuarioMapper usuarioMapper;

    @Override
    public void listarUsuarios() {
        log.info("INICIO");
//        List<Integer> users = usuarioMapper.getUsers();
//        if(users!= null){
//            for(Integer user:users){
//                log.info("usuario ->{}",user);
//            }
//        }
        log.info("FIN");
    }
    
}
