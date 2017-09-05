/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.model.Usuario;
import cl.bennder.entitybennderwebrest.request.DatosPerfilRequest;
import cl.bennder.entitybennderwebrest.request.InfoDatosPerfilRequest;
import cl.bennder.entitybennderwebrest.response.DatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.InfoDatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;

/**
 *
 * @author ext_dayanez
 */
public interface PerfilService {
    
    /***
     * Obtienes los datos de cuenta y personal del usuario
     * @param request
     * @return 
     */
    public DatosPerfilResponse getDatosPerfil(DatosPerfilRequest request);

    /***
     * Permite guardar/actualizar los datos de cuenta y personal del usuario
     * @param request
     * @return 
     */
    public InfoDatosPerfilResponse guardarDatosPerfil(InfoDatosPerfilRequest request);
    
    
    /***
     * Validación de datos de usuarios
     * @param usuario
     * @return 
     */ 
    public ValidacionResponse validaDatosUsuario(Usuario usuario);
}
