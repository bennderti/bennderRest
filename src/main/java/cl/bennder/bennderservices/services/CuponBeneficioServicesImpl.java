/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.constantes.AccionBeneficioUsuario;
import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.model.ParametroSistema;
import cl.bennder.bennderservices.model.UsuarioBeneficio;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.GeneraQrRequest;
import cl.bennder.entitybennderwebrest.request.GetCuponBeneficioRequest;
import cl.bennder.entitybennderwebrest.response.BeneficiosCargadorResponse;
import cl.bennder.entitybennderwebrest.response.GeneraQrResponse;
import cl.bennder.entitybennderwebrest.response.GetCuponBeneficioResponse;
import cl.bennder.entitybennderwebrest.response.UploadBeneficioImagenResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BadElementException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
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
import org.springframework.util.FileCopyUtils;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.core.io.ClassPathResource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import java.io.FileOutputStream;
import com.itextpdf.text.Paragraph;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.crypto.Cipher;
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
    
    @Autowired
    BeneficioMapper beneficioMapper;
    
    @Autowired
    EncriptacionServices encriptacionServices;
    
    @Autowired
    ParametroSistemaServices parametroSistemaServices;
    
    @Autowired
    EmailServices emailServices;
    
    

    @Override
    public Validacion registraAccionBeneficioUsuario(Integer idBeneficio, Integer idUsuario, Integer accion,String codigoBeneficio, Integer cantidad,String codigoBeneficioEncriptado) {
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
            //.- Existe
            if(uBeneficio != null){
                log.info("información actual de accion de usuario beneficio ->{}",uBeneficio.toString());
                //.- existe registro de fecha con determinada accion?
                //.- si(actualizamos)
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
                                Validacion validacion =  this.registraAccionBeneficioUsuario(request.getIdBeneficio(), request.getIdUsuario(), AccionBeneficioUsuario.OBTENIDO, codigo, request.getCantidad(),codEncriptado);
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
    public String generaImagenQR() {
        log.info("inicio");
        String pathImage = null;
        ByteArrayInputStream inStream = null;
        ByteArrayOutputStream bout =
                QRCode.from("https://www.google.cl/?gws_rd=ssl")
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();
        try {
            String path = System.getProperty("java.io.tmpdir");            
            pathImage = path+"/cuponQr.png";
            File f = new File(pathImage);
            log.info("pathImage QR ->{}",pathImage);
            log.info("file(qr) ->{}",f.getAbsolutePath());
            OutputStream out = new FileOutputStream(pathImage);
            bout.writeTo(out);
            pathImage = f.getAbsolutePath();
            //inStream = new ByteArrayInputStream( bout.toByteArray() );
            out.flush();
            out.close();
        } catch (FileNotFoundException e){
            log.error("Error en FileNotFoundException.",e);
            
        } catch (IOException e) {            
            log.error("Error en IOException.",e);
        }
        log.info("fin");
        return pathImage;
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
    public GeneraQrResponse generaCuponQR(GeneraQrRequest request) {
        GeneraQrResponse response = new GeneraQrResponse();
        response.setValidacion(new Validacion("0","1","Problemas al generar cupon QR"));
        log.info("inicio");
        try {
            Map<String, Object> mapa = new HashMap<>();
            String path = this.generaImagenQR();
            //mapa.put("codQR", path);
            mapa.put("aParameter", "HOLA MUNDO!!!!");
            crearPdf(path);
            String key = "Bar12345Bar12345"; // 128 bit key
            String initVector = "RandomInitVector"; // 16 bytes IV
            String encriptar = encriptacionServices.encriptar(key, initVector, "Hello From Chile!!");
            String desencriptar = encriptacionServices.desencriptar(key, initVector, encriptar);
            log.info("key ->{}",key);
            log.info("initVector ->{}",initVector);
            log.info("value ->Hello From Chile!!");
            log.info("encriptar ->{}",encriptar);
            log.info("desencriptar ->{}",desencriptar);
            
            //byte[] pdf = this.generaPdfFromJasper("/jasper/cuponBeneficio.jrxml",mapa);
            byte[] pdf = null;//this.generaPdfFromJasper("/jasper/test.jrxml",mapa);
            //generaPdfFromJasperII("/jasper/test.jrxml",mapa);
            if(pdf != null){     
//                String pathPdf = System.getProperty("java.io.tmpdir");
//                pathPdf = pathPdf+"/cupon.pdf";
//                OutputStream out = new FileOutputStream(pathPdf);
//                out.write(pdf);
//                out.close();
//                log.info("pathPdf->{}",pathPdf);
//                log.info("pdf.length ->{} Bytes",(pdf.length));
                
                /*
                File temporal = File.createTempFile(UUID.randomUUID().toString() + "_", null, new File(directory));
                FileCopyUtils.copy(input, temporal);
                JasperDesign path = JRXmlLoader.load(temporal);
                JasperReport report = JasperCompileManager.compileReport(path);
                JasperPrint print = JasperFillManager.fillReport(report, mapa);
                LOG.info("Se crea el print : {}", print.getName());
                bytesPdfIn = JasperExportManager.exportReportToPdf(print);
                LOG.info("Se crea el bytesPdfIn : {}", bytesPdfIn.length);
                temporal.delete();
                */
            }
            
        } catch (Exception e) {
            log.error("Exception generaCuponQR,",e);
        }
        log.info("fin");
        return response;
        
    }
    public void crearPdf(String imagePath){
        log.info("inicio");
        Document document = new Document();
        try {
            try {
                
                File f = new File(System.getProperty("java.io.tmpdir"), "sample1.pdf");
                //PdfWriter.getInstance(document, new FileOutputStream("sample1.pdf"));
                log.info(".getAbsolutePath()->{}",f.getAbsolutePath());
                PdfWriter.getInstance(document, new FileOutputStream(f.getAbsolutePath()));
                 document.open();
                Image img;
                try {
                    img = Image.getInstance(imagePath);
                    img.setAlignment(Image.ALIGN_CENTER);
      
                    Paragraph p = new Paragraph("Cupón de descuento");
                    p.setAlignment(Element.ALIGN_CENTER);
                    document.add(p);
                    document.add(img);
                    document.close();
                    System.out.println("Done");
                } catch (BadElementException ex) {
                    java.util.logging.Logger.getLogger(CuponBeneficioServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CuponBeneficioServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (FileNotFoundException ex) {
                java.util.logging.Logger.getLogger(CuponBeneficioServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                   
        } catch (DocumentException ex) {
            java.util.logging.Logger.getLogger(CuponBeneficioServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        log.info("fin");
    }
    
   
    
}
