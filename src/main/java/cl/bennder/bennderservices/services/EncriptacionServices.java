/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

/**
 *
 * @author dyanez
 */
public interface EncriptacionServices {
    /***
     * 
     * @param key
     * @param initVector
     * @param value
     * @return 
     */
    public String encriptar(String key, String initVector, String value);
    /***
     * 
     * @param key
     * @param initVector
     * @param encrypted
     * @return 
     */
    public String desencriptar(String key, String initVector, String encrypted);
    
    
}
