package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;
import cl.bennder.entitybennderwebrest.response.BeneficiosResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Diego on 26-03-2017.
 */
@Service
@Transactional
public class BeneficioServicesImpl implements BeneficioServices {

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);

    @Autowired
    BeneficioMapper beneficioMapper;

    /**
     * @author Diego Riveros
     * @param request
     * @return informaction detallada para un beneficio BeneficioResponse
     */
    @Override
    public BeneficioResponse obtenerDetalleBeneficio(BeneficioRequest request) {
        BeneficioResponse response = new BeneficioResponse();
        response.setValidacion(new Validacion("0","1","No existe beneficio seleccionado"));
        log.info("INICIO");

        try {
            response.setBeneficio(beneficioMapper.obtenerDetalleBeneficio(request.getIdBeneficio()));
            if(response != null && response.getBeneficio() != null){
                log.info("Obtención de detalleBeneficio->{}",response.getBeneficio().toString());
            }
            response.setValidacion(new Validacion("0","0","Beneficio OK"));
        } catch (Exception e) {
            log.error("Exception obtenerDetalleBeneficio,",e);
        }
        log.info("fin");
        return response;
    }
}
