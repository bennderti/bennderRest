package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.AccionBeneficioUsuario;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.bennderservices.util.ImagenUtil;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Diego on 26-03-2017.
 */
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class BeneficioServicesImpl implements BeneficioServices {

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);

    @Autowired
    private CuponBeneficioServices cuponBeneficioServices;
    
    
    @Autowired
    private Environment env;
    
    
    @Autowired
    BeneficioMapper beneficioMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
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
            if (request == null)
                return response;

            Beneficio beneficio = beneficioMapper.obtenerDetalleBeneficio(request.getIdBeneficio());
            //.- setean ruta http de imagen de servidor
            log.info("obteniendo ruta http de imagen publicada en servidor...");
            if(beneficio!=null && beneficio.getImagenesBeneficio()!=null && beneficio.getImagenesBeneficio().size() > 0){
                String server = env.getProperty("server");
                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                
                
                //.- Registrando visitas y accion de usuario
                //Integer ,String , Integer ,String ,Integer 
                log.info("Registrando estado de visitas");
                cuponBeneficioServices.registraAccionBeneficioUsuario(request.getIdBeneficio(), request.getIdUsuario(), AccionBeneficioUsuario.VISITADO, null, 0, null, null);
                
                
            }
            //ImagenUtil.convertirImagenesBeneficiosABase64(beneficio);
            response.setBeneficio(beneficio);
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

    @Override
    public void agrearUrlImagenListaBeneficios(List<Beneficio> beneficios) {
        String server = env.getProperty("server");
        
        if(beneficios!=null && !beneficios.isEmpty())
        {
            for(Beneficio beneficio: beneficios)
            {
                if(beneficio.getImagenesBeneficio()!=null && !beneficio.getImagenesBeneficio().isEmpty())
                {
                    for(BeneficioImagen imagen: beneficio.getImagenesBeneficio())
                    {
                        imagen.setUrlImagen(server + imagen.getPath());
                    }
                }                
            }
        }
    }
    
    
}
