package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;

/**
 * Created by Diego on 26-03-2017.
 */
public interface BeneficioServices {

    BeneficioResponse obtenerDetalleBeneficio(BeneficioRequest idBeneficio);
}
