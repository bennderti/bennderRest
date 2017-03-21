/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.LoginRequest;
import cl.bennder.entitybennderwebrest.response.LoginResponse;


/**
 *
 * @author dyanez
 */
public interface UsuarioServices {
    public void listarUsuarios();
    public LoginResponse validacionUsuario(LoginRequest request);
}
