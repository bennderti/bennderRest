/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.CodigoValidacion;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.mapper.CategoriaMapper;
import cl.bennder.bennderservices.model.ParametroSistema;
import cl.bennder.bennderservices.util.ImagenUtil;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.Categoria;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.*;
import cl.bennder.entitybennderwebrest.response.*;

import java.awt.print.Pageable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author dyanez
 */
@PropertySource("classpath:bennder.properties")
@Service
public class CategoriaServicesImpl implements CategoriaServices{

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);
    
    private static final String PAGINACION = "PAGINACION";
    private static final String ITEMS_POR_PAGINA = "ITEMS_POR_PAGINA";
    
    @Autowired
    private Environment env;
            
            
    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private BeneficioMapper beneficioMapper;
             
    @Autowired
    private ParametroSistemaServices parametroSistemaServices;

    @Override
    @Transactional
    public PaginadorBeneficioResponse obtenerBeneficiosPaginados(PaginadorBeneficioRequest request) {
        PaginadorBeneficioResponse response = new PaginadorBeneficioResponse();
        response.setValidacion(new Validacion("0","1","Problemas al obtener beneficios"));
        Integer total = 0;
        log.info("inicio");
        try {
            List<Beneficio> beneficios = new ArrayList<>();
            String server = env.getProperty("server");
            ParametroSistema pItemsPagina = parametroSistemaServices.getDatosParametroSistema(PAGINACION, ITEMS_POR_PAGINA);
            if(pItemsPagina!=null && pItemsPagina.getValorA()!=null&&!pItemsPagina.getValorA().equals("")){
                log.info("items por página ->{}",pItemsPagina.getValorA());
                request.getPaginador().setCantidadPagina(Integer.parseInt(pItemsPagina.getValorA()));
            }
            switch (request.getPaginador().getCategoria().getIdCategoriaPadre()) { 
                        case -1:
                            log.info("es categoría principal...");
                            total = beneficioMapper.obtenerTotalBeneficiosPorCategoriaPadre(request.getPaginador().getCategoria().getIdCategoria());
                            log.info("Total beneficios ->{} para categoria padre ->{}",total,request.getPaginador().getCategoria().getIdCategoria());
                            //beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPadre(categoria.getIdCategoria());
                            beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPadrePaginados(request.getPaginador().getCategoria().getIdCategoria(),request.getPaginador());
                            request.getPaginador().setTotal(total);
                            
                            
                            response.setPaginador(request.getPaginador());
                            log.info("Información paginador->{}",request.getPaginador().toString());
                            for (Beneficio beneficio : beneficios){
                                //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
                                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                            }
                            response.setBeneficios(beneficios);
                            break;
                        default:
                            log.info("es Sub-categoría...");
                            total = beneficioMapper.obtenerTotalBeneficiosPorCategoria(request.getPaginador().getCategoria().getIdCategoria());
                            log.info("Total beneficios ->{} para categoria beneficio ->{}",total,request.getPaginador().getCategoria().getIdCategoria());                            
                            //beneficios = beneficioMapper.obtenerBeneficiosPorCategoria(categoria.getIdCategoria());
                            beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPaginados(request.getPaginador().getCategoria().getIdCategoria(),request.getPaginador());
                            request.getPaginador().setTotal(total);
                            response.setPaginador(request.getPaginador());
                            log.info("Información paginador->{}",request.getPaginador().toString());
                            for (Beneficio beneficio : beneficios){
                                //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
                                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                            }
                            response.setBeneficios(beneficios);
                            break;
                    }
                    //agregarFiltros(beneficios, response);       
            
            
        } catch (Exception e) {
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al obtener beneficios");
            log.error("Exception getBeneficiosByIdCat,",e);
        }
        log.info("fin");
        return response;
    }

    
    
    
    @Override
    @Transactional
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
    @Transactional
    public SubCategoriaProveedorResponse getSubCategoriasProveedor(SubCategoriaProveedorRequest request) {
       SubCategoriaProveedorResponse response = new SubCategoriaProveedorResponse();
       response.setValidacion(new Validacion("0","1","Sin sub-categorias para categoria indicada del proveedor"));
       log.info("inicio");
        try {
            log.info("Datos de entrada ->{}",request.toString());
            response.setSubCategorias(categoriaMapper.obtenerSubCategoriasByIdCatProveedor(request.getIdCategoria(),request.getIdProveedor()));
            response.setValidacion(new Validacion("0","0","Sub-Categorias OK"));
            if(response!=null && response.getSubCategorias()!=null){
                log.info("Obtención de sub-categorias->{}",response.getSubCategorias().size());
            }
        } catch (Exception e) {
            log.error("Exception obtenerCategoriasById,",e);
            response.setValidacion(new Validacion("1","1","Problemas al obtener sub-categorias para categoria indicada del proveedor"));
        }
        log.info("fin");
        return response;
    }

    @Override
    @Transactional
    public BeneficiosResponse filtrarBeneficiosCategoriaPorPrecio(FiltrarBeneficiosRangoRequest request) {
        BeneficiosResponse response = new BeneficiosResponse();
        log.info("INICIO");
        response.setValidacion(new Validacion("0", "1", "No existen beneficios con esta criteria"));
        log.info("Datos de entrada ->{}", request.toString());
        Integer precioMin = request.getRangoMin();
        Integer precioMax = request.getRangoMax();

        Categoria categoria = categoriaMapper.obtenerCategoriaPorId(request.getIdCategoria());
        switch (categoria.getIdCategoriaPadre()) {
            case -1:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatPadreFiltradosPrecio(categoria.getIdCategoriaPadre(), precioMin, precioMax));
                break;
            default:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatFiltradosPorPrecio(categoria.getIdCategoria(), precioMin, precioMax));
                break;
        }
        if (response == null || response.getBeneficios().isEmpty())
            return  response;

        response.setValidacion(new Validacion("0", "0", "filtrarBeneficiosCategoriaPorPrecio OK"));
        log.info("cantidad de beneficios filtrados ->{}", response.getBeneficios().size());

        return response;
    }

    @Override
    public BeneficiosResponse filtrarBeneficiosCategoriaPorDescuento(FiltrarBeneficiosRangoRequest request) {
        BeneficiosResponse response = new BeneficiosResponse();
        log.info("INICIO");
        response.setValidacion(new Validacion("0", "1", "No existen beneficios con esta criteria"));
        log.info("Datos de entrada ->{}", request.toString());
        Integer descuentoMin = request.getRangoMin();
        Integer descuentoMax = request.getRangoMax();

        Categoria categoria = categoriaMapper.obtenerCategoriaPorId(request.getIdCategoria());
        switch (categoria.getIdCategoriaPadre()) {
            case -1:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatPadreFiltradosDescuento(categoria.getIdCategoriaPadre(), descuentoMin, descuentoMax));
                break;
            default:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatFiltradosPorDescuento(categoria.getIdCategoria(), descuentoMin, descuentoMax));
                break;
        }
        if (response == null || response.getBeneficios().isEmpty())
            return  response;

        response.setValidacion(new Validacion("0", "0", "filtrarBeneficiosCategoriaPorDescuento OK"));
        log.info("cantidad de beneficios filtrados ->{}", response.getBeneficios().size());

        return response;
    }

    @Override
    public BeneficiosResponse filtrarBeneficiosPorProveedor(FiltrarBeneficiosRequest request) {
        BeneficiosResponse response = new BeneficiosResponse();
        log.info("INICIO");
        response.setValidacion(new Validacion("0", "1", "No existen beneficios con esta criteria"));
        log.info("Datos de entrada ->{}", request.toString());
        String proveedor = request.getCampoAFiltrar();

        Categoria categoria = categoriaMapper.obtenerCategoriaPorId(request.getIdCategoria());
        switch (categoria.getIdCategoriaPadre()) {
            case -1:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatPadreFiltradosProveedor(categoria.getIdCategoriaPadre(), proveedor));
                break;
            default:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatFiltradosPorProveedor(categoria.getIdCategoria(), proveedor));
                break;
        }
        if (response == null || response.getBeneficios().isEmpty())
            return  response;

        response.setValidacion(new Validacion("0", "0", "filtrarBeneficiosPorProveedor OK"));
        log.info("cantidad de beneficios filtrados ->{}", response.getBeneficios().size());

        return response;
    }

    @Override
    public BeneficiosResponse filtrarBeneficiosPorCalificacion(FiltrarBeneficiosRequest request) {
        BeneficiosResponse response = new BeneficiosResponse();
        log.info("INICIO");
        response.setValidacion(new Validacion("0", "1", "No existen beneficios con esta criteria"));
        log.info("Datos de entrada ->{}", request.toString());
        Integer calificacion = Integer.parseInt(request.getCampoAFiltrar());

        Categoria categoria = categoriaMapper.obtenerCategoriaPorId(request.getIdCategoria());
        switch (categoria.getIdCategoriaPadre()) {
            case -1:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatPadreFiltradosCalificacion(categoria.getIdCategoriaPadre(), calificacion));
                break;
            default:
                response.setBeneficios(beneficioMapper.obtenerBeneficiosCatFiltradosPorCalificacion(categoria.getIdCategoria(), calificacion));
                break;
        }
        if (response == null || response.getBeneficios().isEmpty())
            return  response;

        response.setValidacion(new Validacion("0", "0", "filtrarBeneficiosPorProveedor OK"));
        log.info("cantidad de beneficios filtrados ->{}", response.getBeneficios().size());

        return response;
    }


    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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

    @Transactional
    public CategoriaResponse cargarCategoria(CategoriaByIdRequest request) {
        CategoriaResponse response = new CategoriaResponse();
        response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Problema en validación de usuario"));
        String server = env.getProperty("server");
        Integer total = 0;
        try {
            Integer idCategoria = request.getIdCategoria();
            log.info("Cargando categoria: ->{}", idCategoria);
            ParametroSistema pItemsPagina = parametroSistemaServices.getDatosParametroSistema(PAGINACION, ITEMS_POR_PAGINA);
            if(pItemsPagina!=null && pItemsPagina.getValorA()!=null&&!pItemsPagina.getValorA().equals("")){
                log.info("items por página ->{}",pItemsPagina.getValorA());
                request.getPaginador().setCantidadPagina(Integer.parseInt(pItemsPagina.getValorA()));
            }
            if (idCategoria == null){
                log.error("Campo nombreCategoria esta vacio");
                response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Campo nombreCategoria esta vacio"));
            }
            else {
                Categoria categoria = categoriaMapper.obtenerCategoriaPorId(idCategoria);
                log.info("Buscando por categoria->{}",idCategoria);
                if (categoria == null) {
                    log.info("Objeto categoria esta vacio");
                    response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO, "0", "Objeto categoria esta vacio"));
                } else {
                    log.info("Datos categoria ->{}", categoria.toString());
                    List<Beneficio> beneficios;
                    switch (categoria.getIdCategoriaPadre()) { 
                        case -1:
                            log.info("Cargando categoria padre");
                            response.setCategoriasRelacionadas(categoriaMapper.obtenerSubCategoriasConCantidadBeneficios(categoria.getIdCategoria()));
                            response.setCategoriaPadre(categoria);
                            total = beneficioMapper.obtenerTotalBeneficiosPorCategoriaPadre(categoria.getIdCategoria());
                            log.info("Total beneficios ->{} para categoria padre ->{}",total,categoria.getIdCategoria());
                            //beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPadre(categoria.getIdCategoria());
                            beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPadrePaginados(categoria.getIdCategoria(),request.getPaginador());
                            request.getPaginador().setTotal(total);
                            //categoria.setNombre(nombreCategoria.trim());
                            request.getPaginador().setCategoria(categoria);
                            response.setPaginador(request.getPaginador());
                            log.info("Información paginador->{}",request.getPaginador().toString());
                            for (Beneficio beneficio : beneficios){
                                //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
                                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                            }
                            response.setBeneficios(beneficios);
                            break;
                        default:
                            log.info("Cargando subCategoria");
                            response.setCategoriasRelacionadas(categoriaMapper.obtenerSubCategoriasConCantidadBeneficios(categoria.getIdCategoriaPadre()));
                            response.setCategoriaPadre(categoriaMapper.obtenerCategoriaPorId(categoria.getIdCategoriaPadre()));
                            total = beneficioMapper.obtenerTotalBeneficiosPorCategoria(categoria.getIdCategoria());
                            log.info("Total beneficios ->{} para categoria beneficio ->{}",total,categoria.getIdCategoria());
                            //beneficios = beneficioMapper.obtenerBeneficiosPorCategoria(categoria.getIdCategoria());
                            beneficios = beneficioMapper.obtenerBeneficiosPorCategoriaPaginados(categoria.getIdCategoria(),request.getPaginador());
                            request.getPaginador().setTotal(total);
                           // categoria.setNombre(nombreCategoria.trim());
                            request.getPaginador().setCategoria(categoria);
                            response.setPaginador(request.getPaginador());
                            log.info("Información paginador->{}",request.getPaginador().toString());
                            for (Beneficio beneficio : beneficios){
                                //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
                                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                            }
                            response.setBeneficios(beneficios);
                            break;
                    }
                    agregarFiltros(beneficios, response);

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

    private void agregarFiltros(List<Beneficio> beneficios, CategoriaResponse response) {
        Map<String,Set<String>> filtros = new HashMap<>();
        Set<String> proveedores = new HashSet<>();
        beneficios.forEach(beneficio -> proveedores.add(beneficio.getNombreProveedor()));
        log.info("filtro proveedores size->{}", proveedores.size());
        filtros.put("proveedores", proveedores);
        log.info("filtros cargados para esta categoria ->{}", filtros.size());
        response.setFiltros(filtros);
    }

    @Override
    @Transactional
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
