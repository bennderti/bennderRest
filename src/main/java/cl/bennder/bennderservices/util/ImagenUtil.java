package cl.bennder.bennderservices.util;

import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by Diego on 30-03-2017.
 */
public class ImagenUtil {


    public static void convertirImagenesBeneficiosABase64(Beneficio beneficio) {
        for (BeneficioImagen beneficioImagen : beneficio.getImagenesBeneficio()){
            convertirImagenABase64(beneficioImagen);
        }
    }

    public static void convertirImagenABase64(BeneficioImagen beneficioImagen) {

        beneficioImagen.setImagenBase64(Base64.encodeBase64String(beneficioImagen.getImagen()));
        beneficioImagen.setImagen(null);
    }
}
