/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.EmpresaMapper;
import cl.bennder.bennderservices.mapper.PerfilMapper;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.DatosPerfilRequest;
import cl.bennder.entitybennderwebrest.request.InfoDatosPerfilRequest;
import cl.bennder.entitybennderwebrest.response.DatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.InfoDatosPerfilResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ext_dayanez
 */
@Service
@Transactional
public class PerfilServiceImpl implements PerfilService{
    
    @Autowired
    private PerfilMapper perfilMapper;
    
    
    @Autowired
    private EmpresaMapper empresaMapper;
    

    private static final Logger log = LoggerFactory.getLogger(PerfilServiceImpl.class);

    @Override
    public InfoDatosPerfilResponse guardarDatosPerfil(InfoDatosPerfilRequest request){
        InfoDatosPerfilResponse response = new InfoDatosPerfilResponse();
        response.setValidacion(new Validacion("0","1","Problemas al guardar sucursal"));
        log.info("inicio");
        try {
//            
//            if(request!=null && request.getIdUsuario()!=null){
//                response.setRegiones(perfilMapper.getRegiones());
//                response.setComunas(perfilMapper.getComunas());
//                String mensajeLog = "[idUsuario -> " + request.getIdUsuario() + "] ";
//                
//                
//                log.info("{} Obteniendo datos generales", mensajeLog);
//            }
//            else{
//                log.info("{} Usuario no encontrado ->{}",request.toString());
//                response.getValidacion().setCodigoNegocio("2");
//                response.getValidacion().setMensaje("Usuario no encontrado");
//            }
            
            
//            if(request.getSucursal().getIdSucursal() == null){
//                log.info("creando sucursal...");
//                Integer idDireccion= sucursalMapper.getSeqIdDireccion();
//                log.info("idDireccion ->{}",idDireccion);
//                log.info("guardando direccion...");
//                request.getSucursal().getDireccion().setIdDireccion(idDireccion);
//                sucursalMapper.insertDireccion(request.getSucursal().getDireccion());                
//                Integer idProveedor = proveedorMapper.getIdProveedorByUser(request.getIdUsuario());
//                log.info("guardando datos sucursal para proveedor ->{}",idProveedor);
//                sucursalMapper.insertSucursal(idProveedor, request.getSucursal());
//                response.setValidacion(new Validacion("0","0","Datos sucursal guardados OK."));
//            }
//            else{
//                log.info("editando sucursal...");
//                log.info("actualizando direccion...");
//                sucursalMapper.updateDireccion(request.getSucursal().getDireccion());
//                log.info("actualizando datos sucursal...");
//                sucursalMapper.updateSucursal(request.getSucursal());
//                response.setValidacion(new Validacion("0","0","Datos sucursal actualizado OK."));
//            }            
            log.info("descriptando datos, esquema ->{}",request.getTenantId());
            empresaMapper.cambiarEsquema(request.getTenantId());
            
        } catch (Exception e) {
            response.setValidacion(new Validacion("1","1","Error al guardar sucursal"));
            log.error("Exception guardarSucursal,",e);
        }
        
        log.info("fin");
        return response;
    }
    
    
    
    
    @Override
    public DatosPerfilResponse getDatosPerfil(DatosPerfilRequest request){
        DatosPerfilResponse response = new DatosPerfilResponse();
        response.setValidacion(new Validacion("0","1","Problemas al obtener datos de cuenta"));
        log.info("inicio");
        try {           
            
            if(request!=null && request.getIdUsuario()!=null){
                log.info("datos de entrada ->{}",request.toString());
                String mensajeLog = "[idUsuario -> " + request.getIdUsuario() + "] ";
                
                if(request.getTenantId()!=null){
                    log.info("buscando datos en empresa esquema ->{}",request.getTenantId());
                    empresaMapper.cambiarEsquema(request.getTenantId());
                    log.info("{} Obteniendo datos generales", mensajeLog);
                    response.setRegiones(perfilMapper.getRegiones());
                    response.setComunas(perfilMapper.getComunas());
                    response.setUsuario(perfilMapper.getDatosPerfil(request.getIdUsuario()));
                    if(response.getUsuario() != null){
                        if(response.getUsuario().getDireccion()!=null && response.getUsuario().getDireccion().getIdDireccion()!=null){
                            log.info("{} Obteniendo datos dirección...", mensajeLog);
                            response.getUsuario().setDireccion(perfilMapper.getDireccion(response.getUsuario().getDireccion().getIdDireccion()));
                        }
                        else{
                            log.info("{} Sin datos dirección!!", mensajeLog);
                        }
                        if(response.getUsuario().getContacto()!=null && response.getUsuario().getContacto().getIdContacto()!=null){
                            log.info("{} Obteniendo datos contacto...", mensajeLog);
                            response.getUsuario().setContacto(perfilMapper.getContacto(response.getUsuario().getContacto().getIdContacto()));
                        }
                        else{
                            log.info("{} Sin datos de contacto!!", mensajeLog);
                        }
                        log.info("{} Datos de usuario OK", mensajeLog);
                        response.getValidacion().setCodigoNegocio("0");
                        response.getValidacion().setMensaje("Datos de usuario OK");
                    }
                    else{
                        log.info("{} Usuario no encontrado", mensajeLog);
                        response.getValidacion().setCodigoNegocio("4");
                        response.getValidacion().setMensaje("Usuario no encontrado");
                    }
                }
                else{
                    log.info("{} Empresa no encontrada para usuario", mensajeLog);
                    response.getValidacion().setCodigoNegocio("3");
                    response.getValidacion().setMensaje("Empresa no encontrada para usuario");
                }         
            }
            else{
                log.info("{} Usuario no encontrado ->{}",request.toString());
                log.info("{} Usuario no encontrado ->{}",request.toString());
                response.getValidacion().setCodigoNegocio("2");
                response.getValidacion().setMensaje("Usuario no encontrado");
            }
        } catch (Exception e) {
            response.setValidacion(new Validacion("1","1","Error al obtener datos de cuenta"));
            log.error("Exception getDatosPerfil,",e);
        }
        log.info("fin");
        return response;
    }
    
    
    
}
