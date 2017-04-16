/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.controller.HomeController;
import cl.bennder.bennderservices.mapper.AnuncioMapper;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.entitybennderwebrest.request.CargarHomeRequest;
import cl.bennder.entitybennderwebrest.response.CargarHomeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marcos
 */
@Service
@Transactional
public class HomeServicesImpl implements HomeServices{
    
    private static final Logger log = LoggerFactory.getLogger(HomeServicesImpl.class);
    
    @Autowired
    CategoriaServices categoriaServices;
    
    @Autowired
    AnuncioMapper anuncioMapper;
    
    @Autowired
    BeneficioMapper beneficioMapper;

    @Override
    public CargarHomeResponse cargarHome(CargarHomeRequest request) {
        
        log.info("[cargarHome] - idUsuario " + request.getIdUsuario());
       
        CargarHomeResponse response = new CargarHomeResponse();     
                
        /*Obtener Categorías*/
        response.setCategorias(categoriaServices.getCategorias());
        
        /*Obtener Anuncios*/        
        response.setAnuncios(anuncioMapper.obtenerAnuncios());
        
        /*Obtener Beneficios Preferenciales*/
        response.setBeneficiosDestacados(beneficioMapper.obtenerBeneficiosDestacadosInteresUsuario(request.getIdUsuario()));
        
        /*Obtener ultimos Benefcicios visitados*/
        response.setBeneficiosVisitados(beneficioMapper.obtenerUltimosBeneficiosVistosUsuario(request.getIdUsuario()));
        
        /*Obtener nuevos beneficios del usuario*/
        response.setBeneficiosNuevos(beneficioMapper.obtenerNuevosBeneficiosInteresUsuario(request.getIdUsuario()));        
        
        return response;
    }
    
   
    
}
