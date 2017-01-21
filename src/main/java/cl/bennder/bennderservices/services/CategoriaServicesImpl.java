/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.CategoriaMapper;
import cl.bennder.bennderservices.mapper.UsuarioMapper;
import cl.bennder.bennderservices.model.Validacion;
import cl.bennder.bennderservices.request.CategoriasRequest;
import cl.bennder.bennderservices.response.CategoriasResponse;
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
public class CategoriaServicesImpl implements CategoriaServices{

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);
    
    @Autowired
    private CategoriaMapper categoriaMapper;
    
    @Override
    public CategoriasResponse getCategorias(CategoriasRequest request) {
        CategoriasResponse response = new CategoriasResponse();
        try {
            response.setCategorias(categoriaMapper.getCategorias());
            response.setValidacion(new Validacion("0", "Categorías OK"));
            log.info("Obtención de categorías->{}",response.getCategorias().size());
        } catch (Exception e) {
            log.error("Exception getCategorias,",e);
        }
        
        return response;
    }
 
}
