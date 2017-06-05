/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.controller.HomeController;
import cl.bennder.bennderservices.mapper.AnuncioMapper;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.multitenancy.TenantContext;
import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.entitybennderwebrest.request.CargarHomeRequest;
import cl.bennder.entitybennderwebrest.response.CargarHomeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    
    @Autowired
    BeneficioServices beneficioServices;
    
    

    @Override
    public CargarHomeResponse cargarHome(CargarHomeRequest request) {
      
        if (request == null)
            return null;

        Integer idUsuario = request.getIdUsuario();
       
        CargarHomeResponse response = new CargarHomeResponse();     
                
        /*Obtener Categorías*/
        response.setCategorias(categoriaServices.getCategorias());
        
        log.info("cargarHome-> Cantidad de Categorías{}",response.getCategorias().size());
        
        /*Obtener Anuncios*/        
        response.setAnuncios(anuncioMapper.obtenerAnuncios(TenantContext.getCurrentTenant()));
        
        log.info("cargarHome-> Cantidad de Anuncios{}",response.getAnuncios().size());
        
        /*Obtener Beneficios Preferenciales*/
        response.setBeneficiosDestacados(beneficioMapper.obtenerBeneficiosDestacadosInteresUsuario(idUsuario));
        
        log.info("cargarHome-> Cantidad de Beneficios Destacados{}",response.getBeneficiosDestacados().size());     
        
        /*Obtener ultimos Benefcicios visitados*/
        response.setBeneficiosVisitados(beneficioMapper.obtenerUltimosBeneficiosVistosUsuario(idUsuario));
        
        log.info("cargarHome-> Cantidad de Beneficios Visitados{}",response.getBeneficiosVisitados().size());
        
        /*Obtener nuevos beneficios del usuario*/
        response.setBeneficiosNuevos(beneficioMapper.obtenerNuevosBeneficiosInteresUsuario(idUsuario));
        
        log.info("cargarHome-> Cantidad de Nuevos Beneficios{}",response.getBeneficiosNuevos().size());
        
        beneficioServices.agrearUrlImagenListaBeneficios(response.getBeneficiosDestacados());
        beneficioServices.agrearUrlImagenListaBeneficios(response.getBeneficiosNuevos());
        beneficioServices.agrearUrlImagenListaBeneficios(response.getBeneficiosVisitados());
        
        return response;
    }
    
   
    
}
