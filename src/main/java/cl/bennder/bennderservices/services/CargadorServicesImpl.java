/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.CargadorMapper;
import cl.bennder.entitybennderwebrest.model.BeneficioImagen;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.UploadBeneficioImagenRequest;
import cl.bennder.entitybennderwebrest.response.UploadBeneficioImagenResponse;
import cl.bennder.entitybennderwebrest.utils.UtilsBennder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utilizada para carga imagenes a beneficios
 * @author dyanez
 */
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class CargadorServicesImpl implements CargadorServices{

    
    private static final Logger log = LoggerFactory.getLogger(CargadorServicesImpl.class);
    
    @Autowired
    private Environment env;
    
    @Autowired
    private CargadorMapper cargadorMapper;
    
    

    @Override
    public String guardaImagenSistemaArchivos(byte[] imagen, Integer idProveedor, Integer idMagen, String extension, Integer idBeneficio) {
        log.info("inicio");
        BufferedOutputStream stream = null;
        String ruta = null;
        try {            
            String rutaRaiz = env.getProperty("directorio.imagen.proveedor");
            log.info("rutaRaiz->{}",rutaRaiz);
            log.info("idMagen->{}",idMagen);
            log.info("extension->{}",extension);
            log.info("idProveedor(directorio) ->{} ",idProveedor.toString());
            File directorioProveedor = new File(rutaRaiz + File.separator + idProveedor.toString());
            if (!directorioProveedor.exists()){
                log.info("Directorio proveedor ->{}, no existe, por tanto se crea",idProveedor.toString());
                directorioProveedor.mkdirs();
            }   // Create the file on server
            File directorioBeneficio = new File(rutaRaiz + File.separator + idProveedor.toString()+ File.separator + idBeneficio.toString());
            if (!directorioBeneficio.exists()){
                log.info("Directorio beneficio ->{}, no existe, por tanto se crea",idBeneficio.toString());
                directorioBeneficio.mkdirs();
            }
            
            File serverFile = new File(directorioBeneficio.getAbsolutePath()+ File.separator + idMagen.toString() + "."+extension);
            log.info("serverFile.getAbsolutePath() ->{}",serverFile.getAbsolutePath());
            stream = new BufferedOutputStream(new FileOutputStream(serverFile));            
            if(stream != null){
                log.info("Escribiendo imagen correctamente...");
                ruta = File.separator + idProveedor.toString()+ File.separator + idBeneficio.toString() + File.separator + idMagen.toString()+ "."+extension;
                //ruta = serverFile.getAbsolutePath();
                stream.write(imagen);
                stream.close();
            }
        }catch (FileNotFoundException ex) {
                log.error("Error IOException en guardaImagenSistemaArchivos",ex);
                ruta  = null;
                
        } catch (IOException ex) {
            log.error("Error FileNotFoundException en guardaImagenSistemaArchivos",ex);
            ruta  = null;
        } finally {
            try {
                if(stream != null){
                    stream.close();
                }
            } catch (IOException ex) {
                log.error("Error IOException en guardaImagenSistemaArchivos",ex);
                ruta  = null;
            }
        }
        log.info("fin");
        return ruta;
    }
    
     
    @Override
    public UploadBeneficioImagenResponse uploadImagenesBeneficios(UploadBeneficioImagenRequest request) {
       UploadBeneficioImagenResponse response = new UploadBeneficioImagenResponse();
       response.setValidacion(new Validacion("0","1","Problemas al subir imagenes"));
       log.info("inicio");
        try {
            if(request!=null && request.getBeneficioImagenes()!=null && request.getBeneficioImagenes().size() > 0){
                log.info("request.getBeneficioImagenes().size() ->{}",request.getBeneficioImagenes().size() );
                Integer idBeneficio = request.getBeneficioImagenes().get(0).getIdBeneficio();
               log.info("Cargando imagenes a beneficio(id) ->{}",idBeneficio); 
               log.info("Eliminando imagenes anteriores(base datos)");
               cargadorMapper.eliminarImagenesBeneficio(idBeneficio);
               log.info("Eliminando imagenes de beneficio del proveedor");
               String rutaRaiz = env.getProperty("directorio.imagen.proveedor");
               String directorioBeneficio = rutaRaiz + File.separator + request.getIdProveedor().toString() + File.separator + idBeneficio.toString();
               log.info("Eliminando imagenes directorio de beneficio ->{}",directorioBeneficio);
               UtilsBennder.cleanDirectory(directorioBeneficio, false);
//               
//               File dir = new File(rutaRaiz + File.separator + request.getIdProveedor().toString() + File.separator + idBeneficio.toString());
//               FileUtils.cleanDirectory(dir); 
               
               for(BeneficioImagen bImg : request.getBeneficioImagenes()){
                  log.info("Datos beneficio iamgen a cargar ->{}",bImg.toString());
                  Integer idImagen = cargadorMapper.getSeqIdImagen();
                  String extension = FilenameUtils.getExtension(bImg.getNombre());
                  String path = this.guardaImagenSistemaArchivos(bImg.getImagen(), request.getIdProveedor(), idImagen,extension,idBeneficio);
                  if(path != null){
                      
                      bImg.setIdImagen(idImagen);
                      bImg.setPath(path);
                      log.info("Guandando información de imagen en db ->{}",bImg.toString());
                      cargadorMapper.guardaImagenBeneficio(bImg);
                  }
               }
               response.getValidacion().setCodigoNegocio("0");
               response.getValidacion().setMensaje("Imagenes cargadas correctamente");
               log.info("Imagenes cargadas correctamente para beneficio(id) ->{}",request.getBeneficioImagenes().get(0).getIdBeneficio());
            }
            else{
                response.getValidacion().setCodigoNegocio("1");
                response.getValidacion().setMensaje("Sin imagenes para cargar a beneficio");
                log.info("Sin imagenes para cargar a beneficio");
            }
        } catch (Exception e) {
            response.setValidacion(new Validacion("1","1","Error al subir imagenes"));
            log.error("Exception getBeneficiosByIdCat,",e);
        }
        log.info("fin");
        return response;
    }
    
}
