/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.UploadBeneficioImagenRequest;
import cl.bennder.entitybennderwebrest.response.UploadBeneficioImagenResponse;

/**
 *
 * @author dyanez
 */
public interface CargadorServices {
    
    
    /***
     * Método que permite cargar una lista de imagenes a un beneficio en particular, en donde orden 1 indica que es imagen
     * principal
     * @param request Listado de imagenes asociadas al baneficio
     * @return Respuesta de carga de imagen
     * @author dyanez
     */
    public UploadBeneficioImagenResponse uploadImagenesBeneficios(UploadBeneficioImagenRequest request);
}
