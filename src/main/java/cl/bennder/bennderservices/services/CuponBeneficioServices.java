/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.model.UsuarioBeneficio;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.CanjeaCuponRequest;
import cl.bennder.entitybennderwebrest.request.GeneraCuponQrRequest;
import cl.bennder.entitybennderwebrest.request.GetCuponBeneficioRequest;
import cl.bennder.entitybennderwebrest.response.CanjeaCuponResponse;
import cl.bennder.entitybennderwebrest.response.GeneraCuponQrResponse;
import cl.bennder.entitybennderwebrest.response.GetCuponBeneficioResponse;
import java.util.Map;

/**
 *
 * @author dyanez
 */
public interface CuponBeneficioServices {
    
    /***
     * Método que permite canjear o hacer efectivo el cupón de beneficio, cmbiando estado
     * @param request Contiene código de cupón QR desde link
     * @return Validación de cupón de beneficio
     */
    public CanjeaCuponResponse validaCanjeCuponBeneficio(CanjeaCuponRequest request);
    
    /***
     * Método encargado de crear cupon pdf con información de beneficio
     * @param uBeneficio datos de beneficio y usuario
     * @param rutaImagenQR ruta de la imagen QR a incluir en pdf
     * @param nombrePdf Nombre del pdf del cupon
     * @return PDF de cupon de beneficios
     * @author dyanez
     */
    public byte[] generaCuponPdf(UsuarioBeneficio uBeneficio, String rutaImagenQR,String nombrePdf);
    /***
     * Método que obtiene la información del código de beneficio encriptado
     * @param codigoEncriptadpo Código beneficio encriptado
     * @return Retorna información del código de cupon
     * @author dyanez
     */
    public UsuarioBeneficio desencriptaCodigoBeneficio(String codigoEncriptadpo);
    
    /***
     * Registra acción y envía correo a usuario de beneficio seleccionado
     * @param request Datos de usuario y beneficio
     * @return Respuesta de validación d eobtención de beneficio
     * @author dyanez
     */
    public GetCuponBeneficioResponse getCuponBeneficio(GetCuponBeneficioRequest request);
    
    /***
     * Genera código de beneficio que ha seleccionado usuario
     * @param idBeneficio id beneficio
     * @param idUsuario id de usuario
     * @return Código de beneficio apra usuario que lo ha obtenido/nomenclatura
     * @author dyanez
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
     * @return 
     * @author dyanez
     */
    public Validacion registraAccionBeneficioUsuario(Integer idBeneficio, Integer idUsuario, Integer accion,String codigoBeneficio, Integer cantidad,String codigoBeneficioEncriptado);
    
    /***
     * Método que entrega la ruta de la imangen .png de codigo QR de url de canje
     * @param urlCanjeoCupon url dentro del código QR utilziada para guardar estado de canje cuando se pistolee
     * @param anchoCupon Ancho de imagen de QR
     * @param altoCupon Alto de imagen de QR
     * @param nombreImagenQR Nombre de la imagen QR (PNG)
     * @return Ruta de la imagen QR. Dicho método no elimina imagen por lo que debe ser eliminada despues que se llame
     */
    public String generaImagenCodigoQRBeneficio(String urlCanjeoCupon,int anchoCupon, int altoCupon,String nombreImagenQR);
    public byte[] generaPdfFromJasper(String rutaJrxml,Map<String, Object> mapa);
    /***
     * Método que genera cupon con código QR e información de beneficio
     * @param request información de código de beneficio
     * @author dyanez
     * @return 
     */
    public GeneraCuponQrResponse generaCuponQR(GeneraCuponQrRequest request);
    
}
