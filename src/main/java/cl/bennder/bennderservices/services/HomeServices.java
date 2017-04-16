/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.request.CargarHomeRequest;
import cl.bennder.entitybennderwebrest.response.CargarHomeResponse;

/**
 *
 * @author Marcos
 */

public interface HomeServices {
    
    public CargarHomeResponse cargarHome (CargarHomeRequest request);
    
}
