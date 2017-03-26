package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.services.BeneficioServices;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;
import cl.bennder.entitybennderwebrest.response.BeneficiosResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Diego on 26-03-2017.
 */
@RestController
public class BeneficioController {

    private static final Logger log = LoggerFactory.getLogger(BeneficioController.class);

    @Autowired
    BeneficioServices beneficioServices;

    /**
     * @author Diego Riveros
     * @param request
     * @return informaction detallada para un beneficio BeneficioResponse
     */
    @RequestMapping(value = "obtenerDetalleBeneficio",method = RequestMethod.POST)
    public BeneficioResponse obtenerDetalleBeneficio(@RequestBody BeneficioRequest request) {
        log.info("INICIO");
        BeneficioResponse response = beneficioServices.obtenerDetalleBeneficio(request);
        log.info("response ->{}",response.toString());
        log.info("FIN");
        return response;
    }
}
