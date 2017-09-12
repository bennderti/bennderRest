package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.request.BusquedaRequest;
import cl.bennder.entitybennderwebrest.response.BeneficiosResponse;
import cl.bennder.entitybennderwebrest.response.BusquedaResponse;
import java.util.List;

/**
 * Created by Diego on 26-03-2017.
 */
public interface BeneficioServices {

    BeneficioResponse obtenerDetalleBeneficio(BeneficioRequest idBeneficio);
    void agrearUrlImagenListaBeneficios(List<Beneficio> beneficios);
    BusquedaResponse buscarBeneficios(BusquedaRequest request);
    //BeneficiosResponse obtenerBeneficiosPorCategoria(String nombreCategoria);
}
