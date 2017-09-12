/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.BienvenidoRequest;
import cl.bennder.entitybennderwebrest.request.GuardarDatosBienvenidaRequest;
import cl.bennder.entitybennderwebrest.response.BienvenidoResponse;
import cl.bennder.entitybennderwebrest.response.GuardarDatosBienvenidaResponse;

/**
 *
 * @author Marcos
 */
public interface BienvenidoServices {
    
    BienvenidoResponse obtenerDatosBienvenida (BienvenidoRequest request);
    public GuardarDatosBienvenidaResponse guardarDatosBienvenida (GuardarDatosBienvenidaRequest request);
}
