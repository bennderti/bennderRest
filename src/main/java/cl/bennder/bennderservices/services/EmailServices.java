/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.RecuperacionPasswordRequest;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;


/**
 *
 * @author dyanez
 */
public interface EmailServices {
    public void sendEmail(String to);
    public void sendEmailMimeMessage(String to);
    
    /***
     * Método encargado de enviar un correo con la contraseña del usuario
     * @param request Usuario correo destino
     * @author dyanez
     * @return Validación de recuperación de contraseña
     */
    public ValidacionResponse recuperacionPassword(RecuperacionPasswordRequest request);
    
    /***
     * Método encargado de completar y enviar correo al usuario
     * @param password contraseña del usuario
     * @param usuario usuario bennder
     * @author dyanez
     * @return Validación de envío de correo
     */
    public Validacion completarEnviarCorreoPassWord(String password, String usuario);
    
    /***
     * Permite genear correo para enviar a usuario para su generación de cupon
     * @param idUsuario identificador de usuario, rut sin dv
     * @param idBeneficio identificador del beneficio
     * @param linkUrlDescargaCupon Link de descarga/generación de cupon código QR
     * @author dyanez
     * @return Validación de formación y envio de correo con link para generar/descargar cupón de beneficio
     */
    public ValidacionResponse envioCorreoLinkCuponBeneficio(Integer idUsuario, Integer idBeneficio, String linkUrlDescargaCupon);
    
}
