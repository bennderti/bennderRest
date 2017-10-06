package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.AccionBeneficioUsuario;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.mapper.CategoriaMapper;
import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.bennderservices.util.BusquedaUtil;
import cl.bennder.bennderservices.util.ImagenUtil;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.BeneficioRequest;
import cl.bennder.entitybennderwebrest.request.BusquedaRequest;
import cl.bennder.entitybennderwebrest.response.BeneficioResponse;
import cl.bennder.entitybennderwebrest.response.BusquedaResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Diego on 26-03-2017.
 */
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class BeneficioServicesImpl implements BeneficioServices {

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);
    @Value("${bucketImagenes}")
    String bucketImagenes;

    @Value("${environment}")
    private String environment;

    @Autowired
    private CuponBeneficioServices cuponBeneficioServices;
    
    
    @Autowired
    private Environment env;
    
    
    @Autowired
    BeneficioMapper beneficioMapper;
    @Autowired
    CategoriaMapper categoriaMapper;

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
        log.info("request.getIdBeneficio ->{}",request.getIdBeneficio());
        try {
//            if (request == null)
//                return response;

            Beneficio beneficio = beneficioMapper.obtenerDetalleBeneficio(request.getIdBeneficio());
            
            //.- setean ruta http de imagen de servidor
            log.info("obteniendo ruta http de imagen publicada en servidor...");
            if(beneficio!=null && beneficio.getImagenesBeneficio()!=null && beneficio.getImagenesBeneficio().size() > 0){
                beneficio.setIdBeneficio(request.getIdBeneficio());
                String server = env.getProperty("server");
                //Cambio de url de repositorio de imagenes a Amazon s3
//                ImagenUtil.setUrlImagenesBenecio(server, beneficio);
                ImagenUtil.setUrlImagenesBenecio(bucketImagenes + environment , beneficio);

                
                //.- Registrando visitas y accion de usuario
                //Integer ,String , Integer ,String ,Integer 
                log.info("Registrando estado de visitas");
                cuponBeneficioServices.registraAccionBeneficioUsuario(request.getIdBeneficio(), request.getIdUsuario(), AccionBeneficioUsuario.VISITADO, null, 0, null,null,null);
                
                
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

    @Override
    public BusquedaResponse buscarBeneficios(BusquedaRequest request) {
        
        BusquedaResponse response = new BusquedaResponse();
        
        if(request.getBusqueda()!=null || !request.getBusqueda().isEmpty())
        {
            /*Separar busqueda*/
            String palabrasBusquedaCompleta[] = request.getBusqueda().trim().split(" ");

            /*Eliminar StopWords*/  
            BusquedaUtil busquedaUtil = new BusquedaUtil();

            String palabrasBusqueda = busquedaUtil.eliminacionStopWords(palabrasBusquedaCompleta);

            if(palabrasBusqueda!=null)
            {
               log.info("Palabras de busqueda sin stopwords: {}", palabrasBusqueda);  

               /*Buscar palabras claves en titulos y descripciones*/  
               response.setBeneficios(beneficioMapper.obtenerBeneficiosPorBusqueda(palabrasBusqueda));   

               response.getValidacion().setCodigo("0");
               response.getValidacion().setCodigoNegocio("0");
               response.getValidacion().setMensaje("Se encontraron " + response.getBeneficios().size() + "beneficios ");

            }
            else
            {
               response.getValidacion().setCodigo("0");
               response.getValidacion().setCodigoNegocio("1");
               response.getValidacion().setMensaje("No se econtraron palabras claves para realizar la búsqueda");                
            }
        }
        else
        {
            response.getValidacion().setCodigo("0");
            response.getValidacion().setCodigoNegocio("2");
            response.getValidacion().setMensaje("La busqueda es nula o vacía");
        }
        
        log.info(response.getValidacion().getMensaje());     
        
        return response;
    }

//    @Override
//    public BeneficiosResponse obtenerBeneficiosPorCategoria(String nombreCategoria) {
//        BeneficiosResponse response = new BeneficiosResponse();
//        response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO,"0","Problema en validación de usuario"));
//        String server = env.getProperty("server");
//        log.info("INICIO");
//        try {
//            if (nombreCategoria == null || nombreCategoria.isEmpty()) {
//                log.error("Campo nombreCategoria esta vacio");
//                response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO, "0", "Campo nombreCategoria esta vacio"));
//            } else {
//                Categoria categoria = categoriaMapper.obtenerCategoriaPorNombre(nombreCategoria.trim());
//                if (categoria == null) {
//                    log.error("Objeto categoria esta vacio");
//                    response.setValidacion(new Validacion(CodigoValidacion.ERROR_SERVICIO, "0", "Objeto categoria esta vacio"));
//                } else {
//                    List<Beneficio> beneficios = beneficioMapper.obtenerBeneficiosPorCategoria(categoria.getIdCategoria());
//                    beneficios.forEach(beneficio -> ImagenUtil.setUrlImagenesBenecio(server, beneficio));
//                    response.setBeneficios(beneficios);
//
//                    response.setValidacion(new Validacion("0", "0", "Beneficios OK"));
//                    log.info("Beneficios obtenidos: ->{}", response.getBeneficios().size());
//                }
//            }
//        }
//        catch (Exception e) {
//            log.error("Exception en cargarCategoria,",e);
//        }
//        log.info("FIN");
//        return response;
//    }
}
