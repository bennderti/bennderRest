/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.ProveedorMapper;
import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.DatosGeneralProveedorRequest;
import cl.bennder.entitybennderwebrest.request.ProveedorIdRequest;
import cl.bennder.entitybennderwebrest.response.CategoriasResponse;
import cl.bennder.entitybennderwebrest.response.DatosGeneralProveedorResponse;
import cl.bennder.entitybennderwebrest.response.ProveedoresResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dyanez
 */
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class ProveedorServicesImpl implements ProveedorServices{
    
    private static final Logger log = LoggerFactory.getLogger(ProveedorServicesImpl.class);
    
        
    @Autowired
    private Environment env;
    
    @Autowired
    private ProveedorMapper proveedorMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public String guardaLogoImagenSistemaArchivos(byte[] imagen, Integer idProveedor, String extension) {
        log.info("inicio");
        BufferedOutputStream stream = null;
        String ruta = null;
        try {            
            String rutaRaiz = env.getProperty("directorio.imagen.proveedor");
            String directorioLogo = env.getProperty("directorio.logo.proveedor");
                    
            log.info("rutaRaiz->{}",rutaRaiz);
            log.info("idProveedor->{}",idProveedor);
            log.info("extension->{}",extension);
            log.info("idProveedor(directorio) ->{} ",idProveedor.toString());
            File directorioProveedor = new File(rutaRaiz + File.separator + idProveedor.toString());
            if (!directorioProveedor.exists()){
                log.info("Directorio proveedor ->{}, no existe, por tanto se crea",idProveedor.toString());
                directorioProveedor.mkdirs();
            }   // Create the file on server
            File directorioLogoProveedor = new File(rutaRaiz + File.separator + idProveedor.toString()+ File.separator + directorioLogo);
            if (!directorioLogoProveedor.exists()){
                log.info("Directorio de logo de proveedor ->{}, no existe, por tanto se crea ->{}",idProveedor,directorioLogoProveedor.getAbsolutePath());
                directorioLogoProveedor.mkdirs();
            }
            
            File serverFile = new File(directorioLogoProveedor.getAbsolutePath()+ File.separator + directorioLogo + "."+extension);
            log.info("serverFile.getAbsolutePath() ->{}",serverFile.getAbsolutePath());
            stream = new BufferedOutputStream(new FileOutputStream(serverFile));            
            if(stream != null){
                log.info("Escribiendo imagen correctamente...");
                
                String locationServer = env.getProperty("directorio.imagen.location.server");
                log.info("locationServer(handlerLocation)->{}",locationServer);
                ruta = locationServer  + idProveedor.toString()+ "/logo/logo."+extension;
                //ruta = serverFile.getAbsolutePath();
                stream.write(imagen);
                stream.close();
            }
        }catch (FileNotFoundException ex) {
                log.error("Error IOException en guardaLogoImagenSistemaArchivos",ex);
                ruta  = null;
                
        } catch (IOException ex) {
            log.error("Error FileNotFoundException en guardaLogoImagenSistemaArchivos",ex);
            ruta  = null;
        } finally {
            try {
                if(stream != null){
                    stream.close();
                }
            } catch (IOException ex) {
                log.error("Error IOException en guardaLogoImagenSistemaArchivos",ex);
                ruta  = null;
            }
        }
        log.info("fin");
        return ruta;
    }
    
    

    @Override
    public DatosGeneralProveedorResponse guardaDatosGeneralesProveedor(DatosGeneralProveedorRequest request) {
        DatosGeneralProveedorResponse response = new DatosGeneralProveedorResponse();
        response.setValidacion(new Validacion("0", "1", "Problemas al guardar datos generales del proveedor"));
        
        log.info("inicio");
        try {
            if (request == null)
                return response;
             
            if(request!=null && request.getProveedor()!=null && request.getProveedor().getNombre()!= null
               && request.getProveedor().getRut()!=null){
                String mensajeLog = "[idUsuario -> " + request.getIdUsuario() +"] ";
                log.info("Datos request ->{}",request.toString());
                if(request.getProveedor().getIdProveedor() == null){
                    log.info("{} Validando datos para la creación de proveedor...",mensajeLog);                    
                    if(request.getProveedor().getLogo()!=null && request.getProveedor().getLogo().length > 0){                        
                        //.- obtener proveedor utilizando maximo id
                        
                        //.- Guardando imagen en servidor
                        
                        //.- guardando datos generales de proveedor                        
                        
                    }
                    else{
                        response.getValidacion().setCodigoNegocio("2");
                        response.getValidacion().setMensaje("Favor cargar imagen válida");
                        log.info("{} Favor cargar imagen válida",mensajeLog);
                    }
                }
                else{
                    log.info("{} Validando datos para la actualización de proveedor...",mensajeLog);
                    //.- Guardando imagen en servidor y actualizando pat si viene imagen
                    if(request.getProveedor().getLogo()!=null && request.getProveedor().getLogo().length > 0){
                        log.info("{} Generando logo de imagen en servidor...",mensajeLog);
                        String extension = FilenameUtils.getExtension(request.getProveedor().getNombreImagen());
                        String path = this.guardaLogoImagenSistemaArchivos(request.getProveedor().getLogo(), request.getProveedor().getIdProveedor(), extension);
                        if(path != null){ 
                            request.getProveedor().setPathLogo(path);
                            log.info("{} Actualizando datos de proveedor->{}",mensajeLog,request.getProveedor().getIdProveedor());
                            proveedorMapper.actualizaDatosGeneralesProveedor(request.getProveedor());
                            response.getValidacion().setCodigoNegocio("0");
                            response.getValidacion().setMensaje("Datos actualizados correctamente");
                            log.info("{} Datos actualizados correctamente(OK)",mensajeLog);
                        }
                    }
                    
                }
            }
            else{
                response.getValidacion().setCodigoNegocio("1");
                response.getValidacion().setMensaje("Favor completar los siguientes datos: Nombre, rut(sin dv)");
                log.info("Favor completar los siguientes datos: Nombre, rut(sin dv)");
            }
        } catch (Exception e) {
            log.error("Error en obtenerCategoriaByProveedor",e);
            response.setValidacion(new Validacion("1", "1", "Error al guardar datos generales del proveedor"));
        }
        log.info("fin");        
        return response;
    }

    
    @Override
    public CategoriasResponse obtenerCategoriaByProveedor(ProveedorIdRequest request) {
        CategoriasResponse response = new CategoriasResponse();
        response.setValidacion(new Validacion("0", "1", "Sin categorias para proveedor"));
        
        log.info("inicio");
        try {
            log.info("obtienendo categorias para proveedor ->{}",request.getIdProveedor());
            response.setCategorias(proveedorMapper.obtenerCategoriaProveedor(request.getIdProveedor()));
            response.getValidacion().setCodigoNegocio("0");
            response.getValidacion().setMensaje("Categorias ok para proveedor");
        } catch (Exception e) {
            log.error("Error en obtenerCategoriaByProveedor",e);
            response.setValidacion(new Validacion("1", "1", "Error al obtener categorias para proveedor"));
        }
        log.info("fin");
        
        return response;
    }

    @Override
    public ProveedoresResponse obtenerProveedorHabilitados(ProveedorIdRequest request) {
         
        ProveedoresResponse response = new ProveedoresResponse();
        response.setValidacion(new Validacion("0", "1", "Sin categorias para proveedor"));
        
        log.info("inicio");
        try {
            response.setProveedores(proveedorMapper.obtenerProveedorHabilitados());
            response.getValidacion().setCodigoNegocio("0");
            response.getValidacion().setMensaje("Proveedores OK");
        } catch (Exception e) {
            log.error("Error en obtenerProveedorHabilitados",e);
            response.setValidacion(new Validacion("1", "1", "Error al obtener proveedores habilitados"));
        }
        log.info("fin");
        
        return response;
    }
    
    
    
    
    
}
