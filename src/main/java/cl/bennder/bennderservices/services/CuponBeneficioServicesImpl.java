/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.AccionBeneficioUsuario;
import cl.bennder.bennderservices.constantes.Fuente;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.model.ParametroSistema;
import cl.bennder.bennderservices.model.UsuarioBeneficio;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.CanjeaCuponRequest;
import cl.bennder.entitybennderwebrest.request.GeneraCuponQrRequest;
import cl.bennder.entitybennderwebrest.request.GetCuponBeneficioRequest;
import cl.bennder.entitybennderwebrest.request.ValidacionCuponPOSRequest;
import cl.bennder.entitybennderwebrest.response.CanjeaCuponResponse;
import cl.bennder.entitybennderwebrest.response.GeneraCuponQrResponse;
import cl.bennder.entitybennderwebrest.response.GetCuponBeneficioResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionCuponPOSResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;
import cl.bennder.entitybennderwebrest.utils.UtilsBennder;
import com.itextpdf.text.BadElementException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import java.io.FileOutputStream;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author dyanez
 */
@Service
@Transactional
public class CuponBeneficioServicesImpl implements CuponBeneficioServices{

    private static final Logger log = LoggerFactory.getLogger(CategoriaServicesImpl.class);
    private static final String ENCRIPTACION_COD_BENEFICIO = "ENCRIPTACION_COD_BENEFICIO";
    private static final String PARAMETROS_ENCRIPTACION = "PARAMETROS_ENCRIPTACION";
    private static final String GENERACION_CUPON_QR = "GENERACION_CUPON_QR";
    private static final String URL_DOWNLOAD = "URL_DOWNLOAD";
    private static final String URL_CANJE = "URL_CANJE";
    
    @Autowired
    BeneficioMapper beneficioMapper;
    
    @Autowired
    EncriptacionServices encriptacionServices;
    
    @Autowired
    ParametroSistemaServices parametroSistemaServices;
    
    @Autowired
    EmailServices emailServices;

    @Override
    public ValidacionCuponPOSResponse validacionCuponPOS(ValidacionCuponPOSRequest request) {
       
        ValidacionCuponPOSResponse response = new ValidacionCuponPOSResponse();
        response.setValidacion(new Validacion("0","1","Problemas al validar cupón de beneficio en POS"));
        log.info("inicio");
        try {            
            if(request!=null && request.getCodigoCuponEncriptado()!=null){                
                if(request.getIdVendedor()!=null && request.getIdDireccionSucursal()!=null && request.getPasswordSucursal()!=null){                                   
                    log.info("descriptando datos");
                    UsuarioBeneficio uBeneficio = this.desencriptaCodigoBeneficio(request.getCodigoCuponEncriptado());
                    if(uBeneficio != null){
                        String mensajeLog = "[idUsuario(cupón) -> "+uBeneficio.getIdUsuario()+"] ";
    //                    if( uBeneficio.getIdUsuario().compareTo(request.getIdUsuario()) == 0){
                            //Integer beneficioVigente = beneficioMapper.usuarioHaObtenidoCuponbeneficio(request.getIdUsuario(), uBeneficio.getIdBeneficio());
                            UsuarioBeneficio uBeneficioCanjeado = beneficioMapper.getUsuarioBeneficio(uBeneficio);
                            if(uBeneficioCanjeado!=null){                            
                                //.- validando si cupón ya ha sido canjeado
                                if(uBeneficioCanjeado.getIdAccionBeneficio().compareTo(AccionBeneficioUsuario.CANJEADO) == 0){
                                    log.info("{} Este beneficio ya habia sido canjeado en punto de venta.",mensajeLog);
                                    response.getValidacion().setCodigoNegocio("5");
                                    response.getValidacion().setMensaje("Este beneficio ya habia sido canjeado en punto de venta.");
                                }
                                else{
                                    //.- validamos si password es de surcusal seleccionada
                                    log.info("{} validamos si password es de surcusal seleccionada",mensajeLog);
                                    Beneficio b = this.beneficioMapper.getInfoGeneralBeneficio(uBeneficio.getIdBeneficio());
                                    Integer passSucusalValida = beneficioMapper.esPasswordSucursalValida(request.getPasswordSucursal(), request.getIdDireccionSucursal(), b.getIdProveedor());
                                    if(passSucusalValida > 0){
                                        Validacion vRegistro = this.registraAccionBeneficioUsuario(uBeneficio.getIdBeneficio(), uBeneficio.getIdUsuario(), AccionBeneficioUsuario.CANJEADO, "", 1, "",request.getIdVendedor());
                                        if(vRegistro!=null && "0".equals(vRegistro.getCodigo()) && "0".equals(vRegistro.getCodigoNegocio())){
                                            log.info("{} Cupon válido correctamente para ser canjeado",mensajeLog);
                                            vRegistro.setMensaje("Cupon válido correctamente para ser canjeado");
                                            log.info("{} Formando correo a enviar a usuario - inicio", mensajeLog); 
                                            ValidacionResponse vResponse = emailServices.notificarCanjeCuponBeneficio(uBeneficio.getIdUsuario(), uBeneficio.getIdBeneficio());
                                            response.setValidacion(vResponse.getValidacion());
                                            log.info("{} Formando correo a enviar a usuario - fin", mensajeLog); 
                                        }
                                        else{
                                            log.info("{} Problemas al registrar beneficio", mensajeLog);
                                            response.getValidacion().setCodigoNegocio("3");
                                            response.getValidacion().setMensaje("Problemas al registrar beneficio"); 
                                        }                                    
                                    }
                                    else{

                                        log.info("{} Password ingresada no corresponde al local seleccionado, favor ingresar correctamente", mensajeLog);
                                        response.getValidacion().setCodigoNegocio("4");
                                        response.getValidacion().setMensaje("Password ingresada no corresponde al local seleccionado, favor ingresar correctamente");
                                    }
                                }
                            }
                            else{
                                log.info("{} No existe información de dicho código de beneficio",mensajeLog);
                                response.getValidacion().setCodigoNegocio("3");
                                response.getValidacion().setMensaje("No existe información de dicho código de beneficio");
                            }
                    }
                    else{
                        log.info("No es posible obtener información de beneficio a canjear");
                        response.getValidacion().setCodigoNegocio("2");
                        response.getValidacion().setMensaje("No es posible obtener información de beneficio a canjear");
                    }
                }
                else{
                    log.info("Favor completar datos para validar cupón de descuento (sucursal, password y rut vendedor sin dv.)");
                    response.getValidacion().setCodigoNegocio("1");
                    response.getValidacion().setMensaje("Favor completar datos para validar cupón de descuento (sucursal, password y rut vendedor sin dv.)");
                }
 
            }
            else{
                log.info("Favor completar datos para validar cupón de descuento (codigo beneficio)");
                response.getValidacion().setCodigoNegocio("1");
                response.getValidacion().setMensaje("Favor completar datos para generar cupón de descuento (codigo beneficio)");
            }
            
        } catch (Exception e) {
            log.error("Exception validacionCuponPOS,",e);
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al validar cupón de beneficio en POS");
        }
        log.info("fin");
        return response;
    }

    
    
    @Override
    public CanjeaCuponResponse validaCanjeCuponBeneficio(CanjeaCuponRequest request) {
        //.- Valida datos de entrada
        //.- Descripta codigo de beneficio
        //.- Valida que cupon no haya sido cobrado/canejado anteriormente
        //.- registra estado
        //.- genera respuesta
        CanjeaCuponResponse response = new CanjeaCuponResponse();
        response.setValidacion(new Validacion("0","1","Problemas al canjear cupón de beneficio"));
        log.info("inicio");
        try {            
            
            if(request!=null && request.getCodigoBeneficioEncriptado()!=null){
                log.info("descriptando datos");
                UsuarioBeneficio uBeneficio = this.desencriptaCodigoBeneficio(request.getCodigoBeneficioEncriptado());
                if(uBeneficio != null){
                    String mensajeLog = "[idUsuario(cupón) -> "+uBeneficio.getIdUsuario()+"] ";
//                    if( uBeneficio.getIdUsuario().compareTo(request.getIdUsuario()) == 0){
                        //Integer beneficioVigente = beneficioMapper.usuarioHaObtenidoCuponbeneficio(request.getIdUsuario(), uBeneficio.getIdBeneficio());
                        UsuarioBeneficio uBeneficioCanjeado = beneficioMapper.getUsuarioBeneficio(uBeneficio);
                        if(uBeneficioCanjeado!=null){                            
                            //.- validando si cupón ya ha sido canjeado
                            if(uBeneficioCanjeado.getIdAccionBeneficio().compareTo(AccionBeneficioUsuario.CANJEADO) == 0){
                                log.info("{} Este beneficio ya habia sido canjeado en punto de venta.",mensajeLog);
                                response.getValidacion().setCodigoNegocio("5");
                                response.getValidacion().setMensaje("Este beneficio ya habia sido canjeado en punto de venta.");
                            }
                            else{
//                                Validacion vRegistro = this.registraAccionBeneficioUsuario(uBeneficio.getIdBeneficio(), uBeneficio.getIdUsuario(), AccionBeneficioUsuario.CANJEADO, "", 1, "");
//                                if(vRegistro!=null && "0".equals(vRegistro.getCodigo()) && "0".equals(vRegistro.getCodigoNegocio())){
//                                    log.info("{} Cupon válido correctamente para ser canjeado",mensajeLog);
//                                    vRegistro.setMensaje("Cupon válido correctamente para ser canjeado");
//                                    log.info("{} Formando correo a enviar a usuario - inicio", mensajeLog); 
//                                    ValidacionResponse vResponse = emailServices.notificarCanjeCuponBeneficio(uBeneficio.getIdUsuario(), uBeneficio.getIdBeneficio());
//                                    response.setValidacion(vResponse.getValidacion());
//                                    log.info("{} Formando correo a enviar a usuario - fin", mensajeLog); 
//                                }
//                                else{
//                                   log.info("{} Problemas al registrar beneficio", mensajeLog);
//                                    response.getValidacion().setCodigoNegocio("3");
//                                    response.getValidacion().setMensaje("Problemas al registrar beneficio"); 
//                                }

                                //.- Obteniendo surcusales del proveedor de beneficio
                                response.setSucursales(beneficioMapper.getSucursalesProveedorByBeneficio(uBeneficio.getIdBeneficio()));
                                if(response.getSucursales()!=null && response.getSucursales().size() > 0){
                                    log.info("{} Cupon válido para ser canjeado en sucurcusal (POS)", mensajeLog);
                                    response.getValidacion().setCodigoNegocio("0");
                                    response.getValidacion().setMensaje("Cupon válido para ser canjeado en sucurcusal (POS)");
                                }
                                else{
                                    log.info("{} Sin sucursales configuradas para proveedor de beneficio ->{}", mensajeLog,uBeneficio.getIdBeneficio());
                                    response.getValidacion().setCodigoNegocio("6");
                                    response.getValidacion().setMensaje("Sin sucursales configuradas para proveedor");
                                }
                            }
                        }
                        else{
                            log.info("{} No existe información de dicho código de beneficio",mensajeLog);
                            response.getValidacion().setCodigoNegocio("4");
                            response.getValidacion().setMensaje("No existe información de dicho código de beneficio");
                        }
//                    }
//                    else{
//                        log.info("{} Usuario no corresponde al usuario que ha obtenido el beneficio enviado",mensajeLog);
//                        response.getValidacion().setCodigoNegocio("3");
//                        response.getValidacion().setMensaje("Ud no corresponde al usuario que ha obtenido el beneficio previamente.");
//                    }
                }
                else{
                    log.info("No es posible obtener información de beneficio a canjear");
                    response.getValidacion().setCodigoNegocio("2");
                    response.getValidacion().setMensaje("No es posible obtener información de beneficio a canjear");
                }
            }
            else{
                log.info("Favor completar datos para generar cupón de descuento (codigo beneficio)");
                response.getValidacion().setCodigoNegocio("1");
                response.getValidacion().setMensaje("Favor completar datos para generar cupón de descuento (codigo beneficio)");
            }
            
        } catch (Exception e) {
            log.error("Exception canjearCuponBeneficio,",e);
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al canjear cupón de beneficio");
        }
        log.info("fin");
        return response;
    }
    
    
    

    @Override
    public UsuarioBeneficio desencriptaCodigoBeneficio(String codigoEncriptado) {
        UsuarioBeneficio uBeneficio = null;
        log.info("inicio"); 
        try {
            if(codigoEncriptado!=null && !"".equals(codigoEncriptado) && !"".equals(codigoEncriptado.trim())){
                log.info("codigoEncriptado ->{}",codigoEncriptado);
                log.info("Obteniendo parametros para desencriptar código");
                 ParametroSistema paramEncripCupon = parametroSistemaServices.getDatosParametroSistema(ENCRIPTACION_COD_BENEFICIO, PARAMETROS_ENCRIPTACION);
                 if(paramEncripCupon!=null){
                    String codDesencriptado = encriptacionServices.desencriptar(paramEncripCupon.getValorA(), paramEncripCupon.getValorB(), codigoEncriptado);
                    log.info("codDesencriptado ->{}",codDesencriptado);
                    if(codDesencriptado!=null && !"".equals(codDesencriptado) && !"".equals(codDesencriptado.trim())){
                        String[] param = codDesencriptado.split("-");
                        if(param!=null && param.length == 3){
                            log.info("Datos obtenidos correctamente");
                            //String codigo = idBeneficio+"-"+idUsuario+"-"+Calendar.getInstance().getTime().getTime();
                            uBeneficio = new UsuarioBeneficio();
                            uBeneficio.setIdBeneficio(Integer.parseInt(param[0]));
                            uBeneficio.setIdUsuario(Integer.parseInt(param[1]));
                            uBeneficio.setCodigoBeneficio(codDesencriptado);
                            
                        }
                        else{
                            log.info("código descriptado no cumple formato");
                        }
                    }
                    else{
                        log.info("código descriptado no válido."); 
                    }
                 }
                 else{
                     log.info("sin datos parmetricos para desencriptar"); 
                 }
            }
            else{
                log.info("código nulo o vacio, no válido.");
            }
            
        } catch (Exception e) {
            log.error("Error en desencriptaCodigoBeneficio.",e);
        }
        
        log.info("fin");
        return uBeneficio;
    }
    
    
     
    @Override
    public Validacion registraAccionBeneficioUsuario(Integer idBeneficio, Integer idUsuario, Integer accion,String codigoBeneficio, Integer cantidad,String codigoBeneficioEncriptado, Integer idVendedor) {
        log.info("inicio");        
        //.- reducir stock
        Validacion validacion = new Validacion("1", "0", "Problemas al registrar  beneficio");
        try {
            UsuarioBeneficio uBeneficionQuery = new UsuarioBeneficio();
            uBeneficionQuery.setIdBeneficio(idBeneficio);
            uBeneficionQuery.setIdUsuario(idUsuario);
            log.info("uBeneficionQuery ->{}",uBeneficionQuery.toString());
            log.info("Obtienendo ultima informacion de usuario sobre beneficio(negocio)..");
            UsuarioBeneficio uBeneficio = beneficioMapper.getUsuarioBeneficio(uBeneficionQuery);
            
            if(accion.equals(AccionBeneficioUsuario.CANJEADO)){
                log.info("seteando vendedor que validó cupon de beneficio en POS ->{}",idVendedor);
                uBeneficio.setIdVendedorPOS(idVendedor);
            }
            
            //.- Existe
            if(uBeneficio != null){
                log.info("información actual de accion de usuario beneficio ->{}",uBeneficio.toString());
                //.- existe registro de fecha con determinada accion?
                //.- si(actualizamos)
                uBeneficio.setIdAccionBeneficio(accion);
                beneficioMapper.actualizaAccionUsuarioBeneficio(uBeneficio);
                Integer existeFechaUsuarioBeneficio = beneficioMapper.getFechaUsuarioBeneficio(uBeneficio);
                if(existeFechaUsuarioBeneficio > 0){
                    log.info("actualizamos fecha para accion de beneficio...");
                    beneficioMapper.actualizaFechaAccionUsuarioBeneficio(uBeneficio);
                }
                else{
                    log.info("insertamos fecha para accion de beneficio...");
                    beneficioMapper.insertaFechaAccionUsuarioBeneficio(uBeneficio);
                }

            }
            //.- No existes
            else{
                //.- insertamos
                uBeneficionQuery.setCantidad(cantidad);
                uBeneficionQuery.setIdAccionBeneficio(accion);
                uBeneficionQuery.setCodigoBeneficio(codigoBeneficio);
                uBeneficionQuery.setCodigoBeneficioEncriptado(codigoBeneficioEncriptado);
                if(accion.equals(AccionBeneficioUsuario.OBTENIDO)){
                    log.info("actualizamos stock de beneficio, en donde se reduce en ->{} unidades, para accion de obtenido!",cantidad);
                    beneficioMapper.descuentaStockBeneficio(uBeneficionQuery);
                }

                log.info("insertamos accion beneficio. Datos ->{}",uBeneficionQuery.toString());
                beneficioMapper.guardarUsuarioBeneficio(uBeneficionQuery);
                //.- guardamos auditoria de fecha de beneficio
                log.info("guardamos auditoria de fecha de beneficio...");
                beneficioMapper.insertaFechaAccionUsuarioBeneficio(uBeneficionQuery);
            }
            
            validacion.setCodigo("0");
            validacion.setCodigoNegocio("0");
            validacion.setMensaje("Registro de cupón beneficion OK");
            log.info("Registro de cupón beneficion OK");
            
            
        } catch (Exception e) {
            log.info("Error Exception:",e); 
            validacion.setCodigo("1");
            validacion.setCodigoNegocio("0");
            validacion.setMensaje("Error al registrar  beneficio");
        }        
        log.info("fin");
        return validacion;
    }
    
    
    @Override
    public String generaCodigoCuponBeneficioUsuario(Integer idBeneficio, Integer idUsuario) {        
        String codigo = idBeneficio+"-"+idUsuario+"-"+Calendar.getInstance().getTime().getTime();
        log.info("Código (idBeneficio + idUsuario + time) para beneficio seleccionado por usuario ->{}",codigo);
        return codigo;
    }

    
    @Override
    public GetCuponBeneficioResponse getCuponBeneficio(GetCuponBeneficioRequest request) {
      GetCuponBeneficioResponse response = new GetCuponBeneficioResponse();
       response.setValidacion(new Validacion("0","1","Problemas al obtener benecifio"));
       log.info("inicio");
       String mensajeLog = "";
        try {
            //.- Validamos que usuario no haya seleccionado beneficio previamente - OK
            //.- Generamos código encriptado de beneficio - OK
            //.- Guardamos accion de  beneficio de usuario como estado "obtenido" - OK
            //.- Enviamos correo con url para que cliente pinche y generar cupón beneficion QR            
            if(request != null && request.getIdUsuario()!= null && request.getIdBeneficio() != null){
                mensajeLog = "[idUsuario -> "+request.getIdUsuario()+"] ";
                log.info("Datos de entrada ->{}",request.toString());
                Integer beneficioSeleccionado = beneficioMapper.usuarioHaObtenidoCuponbeneficio(request.getIdUsuario(), request.getIdBeneficio());
                if(beneficioSeleccionado > 0){
                    log.info("{} Ud ya habia obtenido beneficio dentro de periodo de vigencia",mensajeLog);
                    response.getValidacion().setCodigoNegocio("2");
                    response.getValidacion().setMensaje("Ud ya habia obtenido beneficio dentro de periodo de vigencia");
                }
                else{
                    log.info("{} Generando código de beneficio seleccionado por usuario",mensajeLog);
                    String codigo = this.generaCodigoCuponBeneficioUsuario(request.getIdBeneficio(), request.getIdUsuario());
                    log.info("{} Obteniendo parametros para encriptar código",mensajeLog);
                    ParametroSistema paramEncripCupon = parametroSistemaServices.getDatosParametroSistema(ENCRIPTACION_COD_BENEFICIO, PARAMETROS_ENCRIPTACION);
                    if(paramEncripCupon !=null){
                        if(paramEncripCupon.getValorA()!=null && paramEncripCupon.getValorA().length() > 2){
                            log.info("inicio de parametro key ->{}....",paramEncripCupon.getValorA().substring(0, 2));
                        }
                        if(paramEncripCupon.getValorB()!=null && paramEncripCupon.getValorB().length() > 2){
                            log.info("inicio de parametro init vector ->{}...",paramEncripCupon.getValorB().substring(0, 2));
                        }
                        log.info("{} Generando código encriptado...",mensajeLog);
                        String codEncriptado = encriptacionServices.encriptar(paramEncripCupon.getValorA(), paramEncripCupon.getValorB(), codigo);
                        log.info("{} codEncriptado ->{}",mensajeLog,codEncriptado);
                        log.info("{} Generando URL a enviar a usuario.",mensajeLog);
                        ParametroSistema paramUrlCupon = parametroSistemaServices.getDatosParametroSistema(GENERACION_CUPON_QR, URL_DOWNLOAD);
                        if(paramUrlCupon != null){
                            String urlDownloadCupon = paramUrlCupon.getValorA() + codEncriptado;
                            log.info("{} urlDownloadCupon ->{}",mensajeLog,urlDownloadCupon);
                            log.info("{} Registrando estado y accion de usuario sobre beneficio.",mensajeLog);
                            Integer stockBeneficio = beneficioMapper.getStockBeneficio(request.getIdBeneficio());
                            if(stockBeneficio.equals(0)){
                                log.info("Sin stock para beneficio seleccionado.");
                                response.getValidacion().setCodigoNegocio("2");
                                response.getValidacion().setMensaje("Sin stock para beneficio seleccionado");
                            }
                            else{
                                if(stockBeneficio < request.getCantidad()){
                                    log.info("{} Sotck Actual->{} de beneficio ->{} es inferior al seleccionado por usuario, el cual es de ->{}. Por tanto se actualizará con stock actual",mensajeLog,stockBeneficio,request.getIdBeneficio(),request.getCantidad());
                                    request.setCantidad(stockBeneficio);
                                }
                                Validacion validacion =  this.registraAccionBeneficioUsuario(request.getIdBeneficio(), request.getIdUsuario(), AccionBeneficioUsuario.OBTENIDO, codigo, request.getCantidad(),codEncriptado,null);
                                if(validacion!=null && "0".equals(validacion.getCodigo()) && "0".equals(validacion.getCodigoNegocio())){
                                    log.info("{} Formando correo a enviar a usuario - inicio", mensajeLog); 
                                    ValidacionResponse vResponse = emailServices.envioCorreoLinkCuponBeneficio(request.getIdUsuario(), request.getIdBeneficio(), urlDownloadCupon);
                                    response.setValidacion(vResponse.getValidacion());
                                    log.info("{} Formando correo a enviar a usuario - fin", mensajeLog); 
                                }
                                else{
                                   log.info("{} Problemas al registrar beneficio", mensajeLog);
                                    response.getValidacion().setCodigoNegocio("3");
                                    response.getValidacion().setMensaje("Problemas al registrar beneficio"); 
                                }
                            }
                        }
                        else{
                            log.info("{} No existen datos para formar url de descarga/generacion de cupón beneficio",mensajeLog);
                        }
                        
                    }
                    else{
                        log.info("{} No existen datos para encriptar código de beneficio (parámetros sistema)",mensajeLog);
                    }
                }
            }
            else{
                log.info("Favor completar datos para obtener beneficio (usuario/beneficio)");
                response.getValidacion().setCodigoNegocio("1");
                response.getValidacion().setMensaje("Favor completar datos para obtener beneficio (usuario/beneficio)");
            }
            
        } catch (Exception e) {
            response.setValidacion(new Validacion("1","1","Error al obtener benecifio"));
            log.error("Exception getCuponBeneficio,",e);
        }
        log.info("fin");
        return response;
    }

    
    
    
    @Override
    public String generaImagenCodigoQRBeneficio(String urlCanjeoCupon,int anchoCupon, int altoCupon,String nombreImagenQR) {
        log.info("inicio");
        String rutaImagenQR = null;
        try {
        ByteArrayOutputStream bout =
                QRCode.from(urlCanjeoCupon)
                        .withSize(anchoCupon, altoCupon)
                        .to(ImageType.PNG)
                        .stream();
        
            String pathTemporal = System.getProperty("java.io.tmpdir");            
            rutaImagenQR = pathTemporal + nombreImagenQR +".png";
            File f = new File(rutaImagenQR);
            log.info("rutaImagenQR ->{}",rutaImagenQR);
            log.info("f.getAbsolutePath() ->{}",f.getAbsolutePath());
            OutputStream out = new FileOutputStream(rutaImagenQR);
            bout.writeTo(out);
            //rutaImagenQR = f.getAbsolutePath();
            //inStream = new ByteArrayInputStream( bout.toByteArray() );
            out.flush();
            out.close();
        } catch (FileNotFoundException e){
            log.error("Error en FileNotFoundException.",e);
            rutaImagenQR = null;
            
        } catch (IOException e) {            
            log.error("Error en IOException.",e);
            rutaImagenQR = null;
        }
        log.info("fin");
        return rutaImagenQR;
    }
    
    @Override
    public byte[] generaPdfFromJasper(String rutaJrxml,Map<String, Object> mapa) {
        log.info("inicio");
        byte[] bytesPdfOut = null;
        try {
            log.info("cargando jrxml ->{}",rutaJrxml);
            log.info("codQR->{}",mapa.get("codQR"));
            
            JasperDesign jrxml = JRXmlLoader.load(this.getClass().getResourceAsStream(rutaJrxml));
            if(jrxml!=null){
                for(JRParameter j : jrxml.getParameters()){
                    log.info("JRParameter->{}",j.getName());
                }
                log.info("Compilando reporte...");
                JasperReport report = JasperCompileManager.compileReport(jrxml);
                if(report !=null){
                    log.info("Generando print...");
                    JasperPrint print = JasperFillManager.fillReport(report, mapa);
                    if(print != null){
                        log.info("Print generado->{}. Ahora exportando a pdf...",print.getName());
                        bytesPdfOut = JasperExportManager.exportReportToPdf(print);
                        if(bytesPdfOut!=null){
                            log.info("Tamanio de archivo pdf generado->{} Bytes",(bytesPdfOut.length));
                        }
                    }
                }
            }
        } catch (JRException ex) {
            log.error("Error en JRException,",ex);
        }
        return bytesPdfOut;
    }
    
    @Override
    public GeneraCuponQrResponse generaCuponQR(GeneraCuponQrRequest request) {
        GeneraCuponQrResponse response = new GeneraCuponQrResponse();
        response.setValidacion(new Validacion("0","1","Problemas al generar cupon QR"));
        log.info("inicio");
        try {
            
            //.- validando datos - OK
            //.- descriptando código beneficio - OK
            //.- creando código QR
            //.- Creando pdf de información de cupon de beneficio - OK
            //.- generando byte array de pdf - OK
            //.- eliminando datos temporarales - OK
            //.- registrnado estado de cupon descargado/generado - OK
            
            
            if(request!=null && request.getIdUsuario()!=null && request.getCodigoBeneficioEncriptado()!=null){
                String mensajeLog = "[idUsuario -> "+request.getIdUsuario()+"] ";
                UsuarioBeneficio uBeneficio = this.desencriptaCodigoBeneficio(request.getCodigoBeneficioEncriptado());
                if(uBeneficio != null){
                    if( uBeneficio.getIdUsuario().compareTo(request.getIdUsuario()) == 0){
                        //Integer beneficioVigente = beneficioMapper.usuarioHaObtenidoCuponbeneficio(request.getIdUsuario(), uBeneficio.getIdBeneficio());
                        UsuarioBeneficio uBeneficioCanjeado = beneficioMapper.getUsuarioBeneficio(uBeneficio);
                        if(uBeneficioCanjeado!=null){                            
                            //.- validando si cupón ya ha sido canjeado
                            if(uBeneficioCanjeado.getIdAccionBeneficio().compareTo(AccionBeneficioUsuario.CANJEADO) == 0){
                                log.info("{} Este beneficio ya habia sido canjeado en punto de venta.",mensajeLog);
                                response.getValidacion().setCodigoNegocio("4");
                                response.getValidacion().setMensaje("Este beneficio ya habia sido canjeado en punto de venta.");
                            }
                            else{
                                //.-
                                ParametroSistema paramUrlCupon = parametroSistemaServices.getDatosParametroSistema(GENERACION_CUPON_QR, URL_CANJE);
                                if(paramUrlCupon != null){
                                    String urlCanje = paramUrlCupon.getValorA() + request.getCodigoBeneficioEncriptado();
                                    String rutaImagenQR = this.generaImagenCodigoQRBeneficio(urlCanje, 250, 250, uBeneficio.getCodigoBeneficio());
                                    if(rutaImagenQR!=null && !"".equals(rutaImagenQR)){

                                        byte[] bytePdf = this.generaCuponPdf(uBeneficio, rutaImagenQR, uBeneficio.getCodigoBeneficio());
                                        if(bytePdf!=null && bytePdf.length > 0){
                                            Validacion vRegistro = this.registraAccionBeneficioUsuario(uBeneficio.getIdBeneficio(), uBeneficio.getIdUsuario(), AccionBeneficioUsuario.DESCARGADO, "", 1, "",null);
                                            if(vRegistro!=null && "0".equals(vRegistro.getCodigo()) && "0".equals(vRegistro.getCodigoNegocio()) ){
                                                log.info("{} Cupon pdf generado correctamente",mensajeLog);
                                                vRegistro.setMensaje("Cupon pdf generado correctamente");
                                            }
                                            response.setCuponPdf(bytePdf);
                                            response.setValidacion(vRegistro);
                                        }
                                        else{
                                            log.info("{} Cupon no generado",mensajeLog);
                                        }
                                    }
                                    else{
                                        log.info("{} Problemas al generar imagen de beneficio formato QR",mensajeLog);
                                    }
                                }
                                else{
                                   log.info("{} Sin datos de url base para canejo de cupón",mensajeLog); 
                                }
                            }
                            
                        }
                        else{
                            log.info("{} No existe información de dicho beneficio enviada previamente a correo",mensajeLog);
                            response.getValidacion().setCodigoNegocio("4");
                            response.getValidacion().setMensaje("No existe información de dicho beneficio enviada previamente a correo");
                        }
                    }
                    else{
                        log.info("{} Usuario no corresponde al usuario que ha obtenido el beneficio enviado",mensajeLog);
                        response.getValidacion().setCodigoNegocio("3");
                        response.getValidacion().setMensaje("Ud no corresponde al usuario que ha obtenido el beneficio previamente.");
                    }
                }
                else{
                    log.info("{} No es posible obtener información de beneficio enviado",mensajeLog);
                    response.getValidacion().setCodigoNegocio("2");
                    response.getValidacion().setMensaje("No es posible obtener información de beneficio enviado");
                }
            }
            else{
                log.info("Favor completar datos para generar cupón de descuento (usuario/codigo beneficio)");
                response.getValidacion().setCodigoNegocio("1");
                response.getValidacion().setMensaje("Favor completar datos para generar cupón de descuento (usuario/codigo beneficio)");
            }
                        
//            String path = this.generaImagenQR();
//            crearPdf(path);
            
        } catch (Exception e) {
            log.error("Exception generaCuponQR,",e);
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al generar cupon QR");
        }
        log.info("fin");
        return response;
        
    }
    @Override
    public byte[] generaCuponPdf(UsuarioBeneficio uBeneficio, String rutaImagenQR,String nombrePdf){
        
        //.- Informaciónde base de beneficio
        //.- Inclusión de QR en pdf
        //.- eliminación de pdf e imagen temporal
        List<String> pathEliminar = new ArrayList<>();
        
        log.info("inicio");
        byte[] bytePdf = null;
        Document document = new Document();
        try {
            try {
                
                File filePdf = new File(System.getProperty("java.io.tmpdir"), nombrePdf+".pdf");
                //PdfWriter.getInstance(document, new FileOutputStream("sample1.pdf"));
                log.info("filePdf.getAbsolutePath()->{}",filePdf.getAbsolutePath());
                PdfWriter.getInstance(document, new FileOutputStream(filePdf.getAbsolutePath()));
                document.open();
                Image img;
                try {
                    log.info("Obteniendo información de fuentes titulo/desctipción",rutaImagenQR);
                    //.- fuente título
                    BaseFont bFontTitulo = BaseFont.createFont(UtilsBennder.getPathAbsolutaResourcesFile(this.getClass(), Fuente.OPENSANS_SEMIBOLD_TTF), BaseFont.WINANSI, BaseFont.EMBEDDED);
                    Font fontTitulo = new Font(bFontTitulo);
                    fontTitulo.setSize(16);
                    //.- fuente descripción                    
                    BaseFont bFontDescripcion = BaseFont.createFont(UtilsBennder.getPathAbsolutaResourcesFile(this.getClass(), Fuente.OPENSANS_LIGHT_ITALIC_TTF), BaseFont.WINANSI, BaseFont.EMBEDDED);
                    Font fontDescripcion = new Font(bFontDescripcion);
                    //Copy Right
                    Font fontCopyRight = new Font(bFontDescripcion);
                    fontCopyRight.setSize(10);
                    
                    log.info("obteniendo imagen de ruta temporal ->{}",rutaImagenQR);
                    img = Image.getInstance(rutaImagenQR);
                    img.setAlignment(Image.ALIGN_CENTER);
                    log.info("Obteniendo información de beneficio");
                    Beneficio infoBeneficio = beneficioMapper.obtenerDetalleBeneficio(uBeneficio.getIdBeneficio());
                    infoBeneficio.setIdBeneficio(uBeneficio.getIdBeneficio());
                    
                    //.- titulo
                    log.info("título ->{}",infoBeneficio.getTitulo());
                    Paragraph pTitulo = new Paragraph(infoBeneficio.getTitulo(),fontTitulo);
                    pTitulo.setAlignment(Element.ALIGN_CENTER);
                    document.add(pTitulo);
                    //- descripcion
                    log.info("Descripción ->{}",infoBeneficio.getDescripcion());
                    Paragraph pDescripcion = new Paragraph(infoBeneficio.getDescripcion(),fontDescripcion);
                    pDescripcion.setAlignment(Element.ALIGN_CENTER);
                    document.add(pDescripcion);       
                    //.- proveedor
                    log.info("Nombre Proveedor ->{}",infoBeneficio.getNombreProveedor());
                    Paragraph pProveedor = new Paragraph("Local de venta, "+infoBeneficio.getNombreProveedor(),fontDescripcion);
                    pProveedor.setAlignment(Element.ALIGN_CENTER);
                    document.add(pProveedor);                             
                    //.- imagen QR
                    log.info("Agregando imagen QR");
                    Paragraph pPresentar = new Paragraph("Código QR a presentar.",fontDescripcion);
                    pPresentar.setAlignment(Element.ALIGN_CENTER);
                    document.add(pPresentar); 
                    document.add(img);
                    
                    
                    pathEliminar.add(filePdf.getAbsolutePath());
                    pathEliminar.add(rutaImagenQR);
                    
                    //.-imagenes beneficio   
                    
                    if(infoBeneficio.getImagenesBeneficio()!=null && infoBeneficio.getImagenesBeneficio().size() > 0){
                        PdfPTable table = new PdfPTable(1);
                        table.setWidthPercentage(30);
                        table.setHorizontalAlignment(Element.ALIGN_CENTER);
                        int i = 0;
                        for(BeneficioImagen bImagen : infoBeneficio.getImagenesBeneficio()){
                            if(i == 2){
                                break;
                            }
                            else{
                                log.info("agregando imagen a celda...");
                                
                                StringBuilder sb = new StringBuilder();
                                sb.append(Calendar.getInstance().getTime().getTime())
                                  .append(uBeneficio.getIdUsuario())
                                  .append(uBeneficio.getIdUsuario())
                                  .append("img")
                                  .append(""+i)
                                  .append(".png");
                                
                                File fileTemp = new File(System.getProperty("java.io.tmpdir"), sb.toString());
                                UtilsBennder.writeBytesToFile(bImagen.getImagen(), sb.toString());
                                UtilsBennder.writeBytesToFile(bImagen.getImagen(), fileTemp.getAbsolutePath());
                                pathEliminar.add(fileTemp.getAbsolutePath());
                                PdfPCell cell = new PdfPCell();
                                //cell.addElement(Image.getInstance(sb.toString()));
                                cell.addElement(Image.getInstance(fileTemp.getAbsolutePath()));
                                table.addCell(cell);
                            }
                            i++;
                            
                        }
                        document.add(table);
                        log.info("Agregando logo bennder...");
                        Paragraph pCopyRight = new Paragraph("Copyright © 2017 Bennder. Todos los derechos reservados",fontCopyRight);
                        pCopyRight.setAlignment(Element.ALIGN_CENTER);
                        ClassLoader classLoader = getClass().getClassLoader();
                        File fileLogoBennder = new File(classLoader.getResource("cuponPdf/logo-bennder.png").getFile());
                        log.info("fileLogoBennder.getAbsolutePath()->{}",fileLogoBennder.getAbsolutePath());
                        Image imgLogo = Image.getInstance(fileLogoBennder.getAbsolutePath());
                        imgLogo.setAlignment(Image.ALIGN_CENTER);
                        document.add(pCopyRight);                         
                        document.add(imgLogo);
                        
                        
                    }
                    document.close();
                    log.info("Documento close/listo!");
                    log.info("Obteniendo byte[] de cupon pdf");
                    bytePdf = UtilsBennder.readContentIntoByteArray(filePdf);
                    if(bytePdf!=null){
                        log.info("Tamanio de cupon pdf ->{} Bytes",bytePdf.length);
                    }
                    log.info("Eliminando archivos...");
                    if(pathEliminar.size() > 0){
                        
                        for(int j = 0;j<pathEliminar.size() ;j++){
                           UtilsBennder.deleteFile(pathEliminar.get(j)); 
                        }
                    }
                    if(bytePdf != null){
                        log.info("Tamanio de cupon pdf(postEliminación) ->{} Bytes",bytePdf.length);
                    }
//                    Paragraph p = new Paragraph("Cupón de descuento");
//                    p.setAlignment(Element.ALIGN_CENTER);
//                    document.add(p);
//                    document.add(img);
                    
//                    System.out.println("Done");
                    
                    
                    
                } catch (BadElementException ex) {
                    log.error("Error BadElementException",ex);
                } catch (IOException ex) {
                    log.error("Error IOException",ex);
                }

            } catch (FileNotFoundException ex) {
                log.error("Error FileNotFoundException",ex);
            }
                   
        } catch (DocumentException ex) {
            log.error("Error DocumentException",ex);
        }
        log.info("fin");
        return bytePdf;
    }
    
    
    
   
    
}
