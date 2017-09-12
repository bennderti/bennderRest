package cl.bennder.bennderservices.util;

import cl.bennder.bennderservices.services.CategoriaServicesImpl;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * Created by Diego on 30-03-2017.
 */
public class ImagenUtil {
    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);
    
    /***
     * Actualiza url http de imagen en servidor
     * @param server Servidor
     * @param beneficio 
     */
    public static void setUrlImagenesBenecio(String server,Beneficio beneficio){
        if(beneficio!=null && beneficio.getImagenesBeneficio()!=null && beneficio.getImagenesBeneficio().size() > 0){
            for(BeneficioImagen imagen : beneficio.getImagenesBeneficio()){
                imagen.setUrlImagen(server + imagen.getPath());
                log.info("imagen.getUrlImagen() ->{}",imagen.getUrlImagen());
            }
        }
    }
//    public static void convertirImagenesBeneficiosABase64(Beneficio beneficio) {
//        for (BeneficioImagen beneficioImagen : beneficio.getImagenesBeneficio()){
//            convertirImagenABase64(beneficioImagen);
//        }
//    }

//    public static void convertirImagenABase64(BeneficioImagen beneficioImagen) {
//
//        beneficioImagen.setImagenBase64(Base64.encodeBase64String(beneficioImagen.getImagen()));
//        beneficioImagen.setImagen(null);
//    }
        /***
     * Permite obtener el valor correspondiente del properties según sistema operativo
     * @param env
     * @param keyPropertie
     * @return 
     */
    public static String getValuePropertieOS(Environment env,String keyPropertie){
        String os = System.getProperty("os.name");
        log.info("os->{}",os);
        os = os.toLowerCase();
        if(os.contains("windows")){
            return env.getProperty(keyPropertie);
        }
        else{
            return env.getProperty(keyPropertie).replaceAll("C:", "");
        }
    }
}
