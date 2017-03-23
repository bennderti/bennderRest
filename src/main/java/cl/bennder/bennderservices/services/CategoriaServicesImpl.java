/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.CodigoValidacion;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.mapper.CategoriaMapper;
import cl.bennder.entitybennderwebrest.model.Categoria;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.CategoriaByIdRequest;
import cl.bennder.entitybennderwebrest.request.CategoriasRequest;
import cl.bennder.entitybennderwebrest.response.BeneficiosResponse;
import cl.bennder.entitybennderwebrest.response.CategoriaResponse;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
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

    @Autowired
    private BeneficioMapper beneficioMapper;

    @Override
    public BeneficiosResponse getBeneficiosByIdCat(CategoriaByIdRequest request) {
        BeneficiosResponse response = new BeneficiosResponse();
       response.setValidacion(new Validacion("0","1","Sin beneficios"));
       log.info("inicio");
        try {
            response.setBeneficios(beneficioMapper.obtenerBeneficiosPorCategoria(request.getIdCategoria()));
            response.setValidacion(new Validacion("0","0","Beneficios OK"));
            if(response!=null && response.getBeneficios()!=null){
                log.info("Obtención de getBeneficios->{}",response.getBeneficios().size());
            }
        } catch (Exception e) {
            log.error("Exception getBeneficiosByIdCat,",e);
        }
        log.info("fin");
        return response;
    }

    
    @Override
    public CategoriasResponse obtenerCategoriasById(CategoriaByIdRequest request) {
       CategoriasResponse response = new CategoriasResponse();
       response.setValidacion(new Validacion("0","1","Sin Categorias"));
       log.info("inicio");
        try {
            response.setCategorias(categoriaMapper.obtenerCategoriasById(request.getIdCategoria()));
            response.setValidacion(new Validacion("0","0","Categorías OK"));
            if(response!=null && response.getCategorias()!=null){
                log.info("Obtención de categorías->{}",response.getCategorias().size());
            }
        } catch (Exception e) {
            log.error("Exception obtenerCategoriasById,",e);
        }
        log.info("fin");
        return response;
    }

    
    @Override
    public CategoriasResponse getCategorias(CategoriasRequest request) {
        CategoriasResponse response = new CategoriasResponse();
        try {
            response.setCategorias(categoriaMapper.getCategorias());
            response.setValidacion(new Validacion("0","0","Categorías OK"));
            log.info("Obtención de categorías->{}",response.getCategorias().size());
        } catch (Exception e) {
            log.error("Exception getCategorias,",e);
        }
        
        return response;
    }

    @Override
    public CategoriasResponse obtenerCategoriasRelacionadas(CategoriasRequest request) {
        CategoriasResponse response = new CategoriasResponse();

        response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Problema en validación de usuario"));
        try {

            String nombreCategoria = request.getNombreCategoria();


            if (nombreCategoria == null || nombreCategoria.isEmpty()){
                log.error("Campo nombreCategoria esta vacio");
                response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Campo nombreCategoria esta vacio"));
            }
            else {
                Categoria categoria = categoriaMapper.obtenerCategoriaPorNombre(nombreCategoria.trim());
                if (categoria == null) {
                    log.error("Objeto categoria esta vacio");
                    response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Objeto categoria esta vacio"));
                }
                else{
                    switch (categoria.getIdCategoriaPadre()) {
                        case -1:
                            response.setCategorias(categoriaMapper.obtenerSubCategorias(categoria.getIdCategoria()));
                            break;
                        default:
                            response.setCategorias(categoriaMapper.obtenerSubCategorias(categoria.getIdCategoriaPadre()));
                            break;
                    }
                    response.setValidacion(new Validacion("0", "0", "Categorías OK"));
                    log.info("Obtención de categorías->{}", response.getCategorias().size());
                }
            }
        } catch (Exception e) {
            log.error("Exception obtenerCategoriasRelacionadas,",e);
        }
        return response;
    }

    public CategoriaResponse cargarCategoria(CategoriasRequest request) {
        CategoriaResponse response = new CategoriaResponse();
        response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Problema en validación de usuario"));

        try {
            String nombreCategoria = request.getNombreCategoria();
            if (nombreCategoria == null || nombreCategoria.isEmpty()){
                log.error("Campo nombreCategoria esta vacio");
                response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Campo nombreCategoria esta vacio"));
            }
            else {
                Categoria categoria = categoriaMapper.obtenerCategoriaPorNombre(nombreCategoria.trim());
                if (categoria == null) {
                    log.error("Objeto categoria esta vacio");
                    response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO, "0", "Objeto categoria esta vacio"));
                } else {
                    switch (categoria.getIdCategoriaPadre()) {
                        case -1:
                            response.setCategoriasRelacionadas(categoriaMapper.obtenerSubCategorias(categoria.getIdCategoria()));
                            break;
                        default:
                            response.setCategoriasRelacionadas(categoriaMapper.obtenerSubCategorias(categoria.getIdCategoriaPadre()));
                            break;
                    }
                    response.setValidacion(new Validacion("0", "0", "Categorías OK"));
                    log.info("Obtención de categorías->{}", response.getCategoriasRelacionadas().size());

                    response.setBeneficios(beneficioMapper.obtenerBeneficiosPorCategoria(categoria.getIdCategoria()));
                }
            }
        }
        catch (Exception e) {
            log.error("Exception en cargarCategoria,",e);
        }

        return response;
    }

}
