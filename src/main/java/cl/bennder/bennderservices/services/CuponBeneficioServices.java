/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.GeneraQrRequest;
import cl.bennder.entitybennderwebrest.request.GetCuponBeneficioRequest;
import cl.bennder.entitybennderwebrest.response.GeneraQrResponse;
import cl.bennder.entitybennderwebrest.response.GetCuponBeneficioResponse;
import java.util.Map;

/**
 *
 * @author dyanez
 */
public interface CuponBeneficioServices {
    
    
    /***
     * Registra acción y envía correo a usuario de beneficio seleccionado
     * @param request Datos de usuario y beneficio
     * @return Respuesta de validación d eobtención de beneficio
     */
    public GetCuponBeneficioResponse getCuponBeneficio(GetCuponBeneficioRequest request);
    
    /***
     * Genera código de beneficio que ha seleccionado usuario
     * @param idBeneficio id beneficio
     * @param idUsuario id de usuario
     * @return Código de beneficio apra usuario que lo ha obtenido/nomenclatura
     */
    public String generaCodigoCuponBeneficioUsuario(Integer idBeneficio,Integer idUsuario);
    
    /***
     * Registra accion de usuario sobre beneficio
     * @param accion Accion de usuario sobre neficio
     * @param idBeneficio id beneficio
     * @param idUsuario  id de usuario
     * @param codigoBeneficio Codigo de beneficio
     * @param cantidad Cantidad de la unidades de beneficio seleccionada por usuario
     * @param codigoBeneficioEncriptado código beneficio encriptado en AES
     */
    public Validacion registraAccionBeneficioUsuario(Integer idBeneficio, Integer idUsuario, Integer accion,String codigoBeneficio, Integer cantidad,String codigoBeneficioEncriptado);
    //.- generar QR
    //.- generar codigo idenficador beneficio
    public String generaImagenQR();
    public byte[] generaPdfFromJasper(String rutaJrxml,Map<String, Object> mapa);
    public GeneraQrResponse generaCuponQR(GeneraQrRequest request);
    //.- get beneficio (obtencion de beneficio//cambia estado
    //.- download beneficio/ generar pdf y envia a cliente/cambia estado
    //.- guarda estado, accion , fecha de cupon beneficio
    //.- encriptacion código
    
}
