/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.model.Validacion;
import cl.bennder.bennderservices.request.RecuperacionPasswordRequest;
import cl.bennder.bennderservices.response.ValidacionResponse;

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
     * @return Validación de recuperación de contraseña
     */
    public ValidacionResponse recuperacionPassword(RecuperacionPasswordRequest request);
    
    /***
     * Método encargado de completar y enviar correo al usuario
     * @param password contraseña del usuario
     * @param usuario usuario bennder
     * @return Validación de envío de correo
     */
    public Validacion completarEnviarCorreoPassWord(String password, String usuario);
    
}
