/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dyanez
 */
@Service
public class EncriptacionServicesImpl implements EncriptacionServices{

    
    private static final Logger log = LoggerFactory.getLogger(EncriptacionServicesImpl.class);
    
    @Override
    public String encriptar(String key, String initVector, String value) {
        log.info("inicio");
        try {
           IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
           SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
           cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

           byte[] encrypted = cipher.doFinal(value.getBytes());
           String encoded = Base64.encodeBase64String(encrypted);
                   
            log.info("encoded ->{}",encoded);
            log.info("fin");
           return encoded;
        } catch (Exception ex) {
           log.error("Exception e",ex);
        }
         log.info("fin");
        return null;
        
    }

    @Override
    public String desencriptar(String key, String initVector, String encrypted){
        log.info("inicio");
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            String decode = new String(original);
            log.info("decode ->{}",decode);
            return decode;
        } catch (Exception ex) {
            log.error("Exception e",ex);
        }
        log.info("fin");
        return null;
    }
    
}
