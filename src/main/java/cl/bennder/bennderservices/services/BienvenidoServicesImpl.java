/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.CategoriaMapper;
import cl.bennder.bennderservices.mapper.UsuarioMapper;
import cl.bennder.entitybennderwebrest.request.BienvenidoRequest;
import cl.bennder.entitybennderwebrest.request.GuardarDatosBienvenidaRequest;
import cl.bennder.entitybennderwebrest.response.BienvenidoResponse;
import cl.bennder.entitybennderwebrest.response.GuardarDatosBienvenidaResponse;
import cl.bennder.entitybennderwebrest.model.Categoria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Marcos
 */
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class BienvenidoServicesImpl implements BienvenidoServices{
    
    private static final Logger log = LoggerFactory.getLogger(BienvenidoServicesImpl.class);

    @Autowired
    private CategoriaMapper categoriaMapper;
    
    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @Override
    public BienvenidoResponse obtenerDatosBienvenida(BienvenidoRequest request) 
    {
        log.info("INICIO");
        BienvenidoResponse response = new BienvenidoResponse();
        
        response.setCategorias(categoriaMapper.obtenerTodasCategorias());
        
        log.info("Cantidad de categorias padre obtenidas: " + response.getCategorias().size());
        
        for(Categoria categoria : response.getCategorias())
            log.info("Categoría: " + categoria.getNombre() + " - Cantidad de subcategorias: " + categoria.getSubCategorias().size());
        
        log.info("FIN");
        return response;
    }       

    @Override
    public GuardarDatosBienvenidaResponse guardarDatosBienvenida(GuardarDatosBienvenidaRequest request) 
    {
        log.info("INICIO");
        GuardarDatosBienvenidaResponse response = new GuardarDatosBienvenidaResponse();
        
        try{
            if(request.getListaCategoriasSelec()!=null && !request.getListaCategoriasSelec().isEmpty())
            {                
                if(request.getIdUsuario()!=null)
                {
                    log.info("idUsuario: " + request.getIdUsuario());
                    
                    for(int i = 0; i<request.getListaCategoriasSelec().size(); i++)
                    {
                        log.info("SubCategoría seleccionada: " + request.getListaCategoriasSelec().get(i));
                        categoriaMapper.guardarInteresUsuario(request.getListaCategoriasSelec().get(i), request.getIdUsuario());
                    }

                    response.getValidacion().setCodigo("0");
                    response.getValidacion().setCodigoNegocio("0");
                    response.getValidacion().setMensaje("Las categorías preferenciales se han guardado exitosamente");
                    log.info(response.getValidacion().getMensaje());
                    
                    usuarioMapper.actualizarEstadoUsuario(request.getIdUsuario(), 2);
                    log.info("El usuario {} queda en estado activo(2)", request.getIdUsuario());
                }
                else
                {
                    response.getValidacion().setCodigo("0");
                    response.getValidacion().setCodigoNegocio("1");
                    response.getValidacion().setMensaje("El usuario es nulo");
                    log.info(response.getValidacion().getMensaje());
                }                
            }
            else
            {
                response.getValidacion().setCodigo("0");
                response.getValidacion().setCodigoNegocio("2");
                response.getValidacion().setMensaje("La lista de las categorias seleccionadas es vacía o nula");
                log.info(response.getValidacion().getMensaje());
            }
        }
        catch(Exception e)
        {
            log.error("Exception guardarDatosBienvenida: ", e);
            
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("3");
            response.getValidacion().setMensaje("Ocurrió un error inesperado al guardar las categorías preferenciales");
            log.info(response.getValidacion().getMensaje());
            
            if(request.getIdUsuario()!=null){
                categoriaMapper.eliminarTodosInteresUsuario(request.getIdUsuario());
                log.info("Se eliminan todas las posibles categorías preferenciales guardadas al usuario {}", request.getIdUsuario());
            }
        }    
       
        log.info("FIN");
        return response;
    }
}
