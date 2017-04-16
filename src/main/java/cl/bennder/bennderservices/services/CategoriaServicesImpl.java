/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.CodigoValidacion;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.mapper.CategoriaMapper;
import cl.bennder.bennderservices.util.ImagenUtil;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.Categoria;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.CategoriaByIdRequest;
import cl.bennder.entitybennderwebrest.request.CategoriasRequest;
import cl.bennder.entitybennderwebrest.response.BeneficiosCargadorResponse;
import cl.bennder.entitybennderwebrest.response.CategoriaResponse;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author dyanez
 */
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class CategoriaServicesImpl implements CategoriaServices{

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);
    
    
    @Autowired
    private Environment env;
            
            
    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private BeneficioMapper beneficioMapper;

    @Override
    public BeneficiosCargadorResponse getBeneficiosByIdCat(CategoriaByIdRequest request) {
        BeneficiosCargadorResponse response = new BeneficiosCargadorResponse();
        response.setValidacion(new Validacion("0","1","Sin beneficios"));
       log.info("inicio");
        try {
            response.setBeneficios(beneficioMapper.getBeneficiosCargadorByIdCat(request.getIdCategoria()));
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
        String server = env.getProperty("server");
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
                    List<Beneficio> beneficios;
                    switch (categoria.getIdCategoriaPadre()) {
                        case -1:
                            response.setCategoriasRelacionadas(categoriaMapper.obtenerSubCategoriasConCantidadBeneficios(categoria.getIdCategoria()));
                            response.setCategoriaPadre(categoria);
                            beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPadre(categoria.getIdCategoria());
                            for (Beneficio beneficio : beneficios){
                                //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
                                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                            }
                            response.setBeneficios(beneficios);
                            break;
                        default:
                            response.setCategoriasRelacionadas(categoriaMapper.obtenerSubCategoriasConCantidadBeneficios(categoria.getIdCategoriaPadre()));
                            response.setCategoriaPadre(categoriaMapper.obtenerCategoriaPorId(categoria.getIdCategoriaPadre()));
                            beneficios = beneficioMapper.obtenerBeneficiosPorCategoria(categoria.getIdCategoria());
                            
                            for (Beneficio beneficio : beneficios){
                                //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
                                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                            }
                            response.setBeneficios(beneficios);
                            break;
                    }
                    response.setValidacion(new Validacion("0", "0", "Categorías OK"));
                    log.info("Obtención de categorías->{}", response.getCategoriasRelacionadas().size());


                }
            }
        }
        catch (Exception e) {
            log.error("Exception en cargarCategoria,",e);
        }

        return response;
    }

    @Override
    public List<Categoria> getCategorias() {
        
        List<Categoria> response = new ArrayList<>();
        
        try {
            response = categoriaMapper.getCategorias();
            log.info("Obtención de categorías->{}",response.size());
        } catch (Exception e) {
            log.error("Exception getCategorias,",e);
        }
        return response;
    }

}
