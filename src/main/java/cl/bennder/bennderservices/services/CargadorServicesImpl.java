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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Utilizada para carga imagenes a beneficios
 * @author dyanez
 */
@Service
@Transactional
public class CargadorServicesImpl implements CargadorServices{

    
    private static final Logger log = LoggerFactory.getLogger(CargadorServicesImpl.class);
    
    @Autowired
    private CargadorMapper cargadorMapper;
     
    @Override
    public UploadBeneficioImagenResponse uploadImagenesBeneficios(UploadBeneficioImagenRequest request) {
       UploadBeneficioImagenResponse response = new UploadBeneficioImagenResponse();
       response.setValidacion(new Validacion("0","1","Problemas al subir imagenes"));
       log.info("inicio");
        try {
            if(request!=null && request.getBeneficioImagenes()!=null && request.getBeneficioImagenes().size() > 0){
                
               log.info("Cargando imagenes a beneficio(id) ->{}",request.getBeneficioImagenes().get(0).getIdBeneficio()); 
               log.info("Eliminando imagenes anteriores");
               cargadorMapper.eliminarImagenesBeneficio(request.getBeneficioImagenes().get(0).getIdBeneficio());
               for(BeneficioImagen bImg : request.getBeneficioImagenes()){
                  log.info("Datos beneficio a cargar ->{}",bImg.toString());
                  cargadorMapper.guardaImagenBeneficio(bImg);
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
