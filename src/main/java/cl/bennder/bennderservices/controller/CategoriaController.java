/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.services.BeneficioServices;
import cl.bennder.bennderservices.services.CategoriaServices;
import cl.bennder.entitybennderwebrest.request.*;
import cl.bennder.entitybennderwebrest.response.BeneficiosCargadorResponse;
import cl.bennder.entitybennderwebrest.response.BeneficiosResponse;
import cl.bennder.entitybennderwebrest.response.CategoriaResponse;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import cl.bennder.entitybennderwebrest.response.SubCategoriaProveedorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author dyanez
 */
@RestController
public class CategoriaController {

    private static final Logger log = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaServices categoriaServices;
    @Autowired
    private BeneficioServices beneficioServices;

    @RequestMapping(value = "obtenerCategoriasById", method = RequestMethod.POST)
    public CategoriasResponse obtenerCategoriasById(@RequestBody CategoriaByIdRequest request) {
        log.info("INICIO");
        CategoriasResponse response = categoriaServices.obtenerCategoriasById(request);
        log.info("response ->{}", response.toString());
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "getBeneficiosByIdCat", method = RequestMethod.POST)
    public BeneficiosCargadorResponse getBeneficiosByIdCat(@RequestBody CategoriaByIdRequest request) {
        log.info("INICIO");
        BeneficiosCargadorResponse response = categoriaServices.getBeneficiosByIdCat(request);
        log.info("response ->{}", response.toString());
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "categoria/getSubCategoriasProveedor", method = RequestMethod.POST)
    public SubCategoriaProveedorResponse getSubCategoriasProveedor(@RequestBody SubCategoriaProveedorRequest request) {
        log.info("INICIO");
        SubCategoriaProveedorResponse response = categoriaServices.getSubCategoriasProveedor(request);
        log.info("response ->{}", response.toString());
        log.info("FIN");
        return response;
    }


    //.- login
    @RequestMapping(value = "getCategorias", method = RequestMethod.POST)
    public CategoriasResponse getCategorias(@RequestBody CategoriasRequest request) {
        log.info("INICIO");
        CategoriasResponse response = categoriaServices.getCategorias(request);
        log.info("response ->{}", response.toString());
        log.info("FIN");
        return response;
    }

//    @RequestMapping(value = "obtenerBeneficiosPorCategoria", method = RequestMethod.POST)
//    public BeneficiosResponse obtenerBeneficiosPorCategoria(@RequestBody CategoriasRequest request) {
//        log.info("INICIO");
//        BeneficiosResponse response = beneficioServices.obtenerBeneficiosPorCategoria(request.getNombreCategoria());
//        log.info("FIN");
//        return response;
//    }

    @RequestMapping(value = "obtenerCategoriasRelacionadas", method = RequestMethod.POST)
    public CategoriasResponse obtenerCategoriasRelacionadas(@RequestBody CategoriasRequest request) {
        log.info("INICIO");
        CategoriasResponse response = categoriaServices.obtenerCategoriasRelacionadas(request);
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "cargarCategoria", method = RequestMethod.POST)
    public CategoriaResponse cargarCategoria(@RequestBody CategoriasRequest request) {
        log.info("INICIO");
        CategoriaResponse response = categoriaServices.cargarCategoria(request);
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "/categoria/obtenerBeneficiosCategoriaFiltradosPorPrecio", method = RequestMethod.POST)
    public BeneficiosResponse obtenerBeneficiosCategoriaFiltradosPorPrecio(@RequestBody FiltrarBeneficiosRangoRequest request) {
        log.info("INICIO");
        BeneficiosResponse response = categoriaServices.filtrarBeneficiosCategoriaPorPrecio(request);
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "/categoria/obtenerBeneficiosCategoriaFiltradosPorDescuento", method = RequestMethod.POST)
    public BeneficiosResponse obtenerBeneficiosCategoriaFiltradosPorDescuento(@RequestBody FiltrarBeneficiosRangoRequest request) {
        log.info("INICIO");
        BeneficiosResponse response = categoriaServices.filtrarBeneficiosCategoriaPorDescuento(request);
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "/categoria/filtrarBeneficiosPorProveedor", method = RequestMethod.POST)
    public BeneficiosResponse filtrarBeneficiosPorProveedor(@RequestBody FiltrarBeneficiosRequest request) {
        log.info("INICIO");
        BeneficiosResponse response = categoriaServices.filtrarBeneficiosPorProveedor(request);
        log.info("FIN");
        return response;
    }

    @RequestMapping(value = "/categoria/filtrarBeneficiosPorCalificacion", method = RequestMethod.POST)
    public BeneficiosResponse filtrarBeneficiosPorCalificacion(@RequestBody FiltrarBeneficiosRequest request) {
        log.info("INICIO");
        BeneficiosResponse response = categoriaServices.filtrarBeneficiosPorCalificacion(request);
        log.info("FIN");
        return response;
    }
}
