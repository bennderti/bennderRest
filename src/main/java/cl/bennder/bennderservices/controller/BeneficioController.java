package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.bennderservices.services.BeneficioServices;
import cl.bennder.bennderservices.services.CuponBeneficioServices;
import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.request.BusquedaRequest;
import cl.bennder.entitybennderwebrest.request.CanjeaCuponRequest;
import cl.bennder.entitybennderwebrest.request.GeneraCuponQrRequest;
import cl.bennder.entitybennderwebrest.request.GetCuponBeneficioRequest;
import cl.bennder.entitybennderwebrest.request.ValidacionCuponPOSRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;
import cl.bennder.entitybennderwebrest.response.BusquedaResponse;
import cl.bennder.entitybennderwebrest.response.CanjeaCuponResponse;
import cl.bennder.entitybennderwebrest.response.GeneraCuponQrResponse;
import cl.bennder.entitybennderwebrest.response.GetCuponBeneficioResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionCuponPOSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Diego on 26-03-2017.
 */
@RestController
public class BeneficioController {

    private static final Logger log = LoggerFactory.getLogger(BeneficioController.class);

    @Autowired
    BeneficioServices beneficioServices;
    
    @Autowired
    CuponBeneficioServices cuponBeneficioServices;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    /***
     * Méotodo que permite validar el canje de cupón de beneficio en POS
     * @param request Datos para validación en POS
     * @return Validación de cupón de de beneficio en POS
     */
    @RequestMapping(value = "beneficio/validacionCuponPOS",method = RequestMethod.POST)
    public @ResponseBody ValidacionCuponPOSResponse validacionCuponPOS(@RequestBody ValidacionCuponPOSRequest request){
        log.info("INICIO");
        ValidacionCuponPOSResponse response = cuponBeneficioServices.validacionCuponPOS(request);
        log.info("FIN");
        return response;
    }
    /***
     * Servicio expuesto que permite validar/canjear cupion de beneficio al pistolear/escanear código QR
     * @param request Datos código beneficio
     * @return Validación de canje cupón
     */
    @RequestMapping(value = "beneficio/validaCanjeCupon",method = RequestMethod.POST)
    public @ResponseBody CanjeaCuponResponse validaCanjeCupon(@RequestBody CanjeaCuponRequest request){
        log.info("INICIO");
        CanjeaCuponResponse response = cuponBeneficioServices.validaCanjeCuponBeneficio(request);
        log.info("FIN");
        return response;
    }
    
    /***
     * Permite generar cupón pdf código QR
     * @param request Datos código beneficio
     * @return Archivo pdf en formato byte
     */
    @RequestMapping(value = "beneficio/generaCuponQR",method = RequestMethod.POST)
    public @ResponseBody GeneraCuponQrResponse generaCuponQR(@RequestBody GeneraCuponQrRequest request,HttpServletRequest req){
        log.info("INICIO");
        request.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(req));
        GeneraCuponQrResponse response = cuponBeneficioServices.generaCuponQR(request);
        log.info("FIN");
        return response;
    }
    
    /***
     * Permite obtener y enviar correo a usuario con link para generar cupon beneficio QR
     * @param request datos de beneficio/usuario/cantidad(unidades)
     * @return 
     */
    @RequestMapping(value = "beneficio/getCuponBeneficio",method = RequestMethod.POST)
    public @ResponseBody GetCuponBeneficioResponse getCuponBeneficio(@RequestBody GetCuponBeneficioRequest request,HttpServletRequest req) {
        log.info("INICIO");
        request.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(req));
        //request.setTenantUser(UtilBennderRest.getTenantId(req));
        GetCuponBeneficioResponse response = cuponBeneficioServices.getCuponBeneficio(request);
        log.info("FIN");
        return response;
    }

    /**
     * @author Diego Riveros
     * @param beneficioRequest
     * @param request
     * @return informaction detallada para un beneficio BeneficioResponse
     */
    @RequestMapping(value = "obtenerDetalleBeneficio",method = RequestMethod.POST)
    public @ResponseBody BeneficioResponse obtenerDetalleBeneficio(@RequestBody BeneficioRequest beneficioRequest, HttpServletRequest request) {
        log.info("INICIO");

        //obteniendo idUsuario desde token
        beneficioRequest.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(request));
        log.debug("idUsuario -> " + beneficioRequest.getIdUsuario());
        BeneficioResponse response = beneficioServices.obtenerDetalleBeneficio(beneficioRequest);
        log.info("response ->{}",response.toString());
        log.info("FIN");
        return response;
    }
    
    @RequestMapping(value = "beneficio/buscarBeneficios",method = RequestMethod.POST)
    public BusquedaResponse buscarBeneficios(@RequestBody BusquedaRequest request){
        log.info("INICIO");
        BusquedaResponse response = beneficioServices.buscarBeneficios(request);
        log.info("FIN");
        return response;
    }    
}
