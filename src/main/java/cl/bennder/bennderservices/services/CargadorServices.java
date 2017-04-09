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
    
    
    /***
     * Método que permite guardar la imagen en sistema de archivos, es decir directorio del Sistema Operativo,
     * definido en standalone.xml location:ImagesDirHandlerLinux
     * @param idProveedor Identificador directorio de proveedor
     * @param imagen Image en byte array
     * @param idMagen identificador de imagen
     * @param extension Extensión de la imagen
     * @param idBeneficio Identificador de beneficio (utilizado para directorio)
     * @return Ruta del archivo almacenado
     */
    public String guardaImagenSistemaArchivos(byte[] imagen, Integer idProveedor, Integer idMagen,String extension,Integer idBeneficio);
    
    
    
}
