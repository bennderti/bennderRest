/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.controller.HomeController;
import cl.bennder.bennderservices.mapper.AnuncioMapper;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.security.JwtTokenUtil;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public CargarHomeResponse cargarHome(CargarHomeRequest request) {

        if (request == null)
            return null;

        //obteniendo idUsuario desde token
        Integer idUsuario = jwtTokenUtil.getIdUsuarioFromToken(request.getToken());

        log.info("[cargarHome] - idUsuario " + idUsuario);
       
        CargarHomeResponse response = new CargarHomeResponse();     
                
        /*Obtener Categorías*/
        response.setCategorias(categoriaServices.getCategorias());
        
        /*Obtener Anuncios*/        
        response.setAnuncios(anuncioMapper.obtenerAnuncios());
        
        /*Obtener Beneficios Preferenciales*/
        response.setBeneficiosDestacados(beneficioMapper.obtenerBeneficiosDestacadosInteresUsuario(idUsuario));
        
        /*Obtener ultimos Benefcicios visitados*/
        response.setBeneficiosVisitados(beneficioMapper.obtenerUltimosBeneficiosVistosUsuario(idUsuario));
        
        /*Obtener nuevos beneficios del usuario*/
        response.setBeneficiosNuevos(beneficioMapper.obtenerNuevosBeneficiosInteresUsuario(idUsuario));
        
        return response;
    }
    
   
    
}
