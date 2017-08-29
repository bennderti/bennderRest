/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.EmpresaMapper;
import cl.bennder.bennderservices.mapper.PerfilMapper;
import cl.bennder.entitybennderwebrest.model.Usuario;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.DatosPerfilRequest;
import cl.bennder.entitybennderwebrest.request.InfoDatosPerfilRequest;
import cl.bennder.entitybennderwebrest.response.DatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.InfoDatosPerfilResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;
import javax.servlet.DispatcherType;
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
    public ValidacionResponse validaDatosUsuario(Usuario usuario) {
        InfoDatosPerfilResponse response = new InfoDatosPerfilResponse();
        response.setValidacion(new Validacion("0","1","Problemas al guardar datos de perfil"));
        log.info("inicio");
        try {
            
            if(usuario == null){                
                response.getValidacion().setCodigoNegocio("2");
                response.getValidacion().setMensaje("Favor completar los datos de perfil");
                log.info("Favor completar los datos de perfil");
                return response;
            }
            log.info("Validando datos generales...");
            if(usuario.getNombres() == null || usuario.getNombres().trim().equals("")){                
                response.getValidacion().setCodigoNegocio("3");
                response.getValidacion().setMensaje("Favor completar nombres");
                log.info("Favor completar los datos de perfil");
                return response;
            }
            if(usuario.getApellidoM()== null || usuario.getApellidoM().trim().equals("")){                
                response.getValidacion().setCodigoNegocio("4");
                response.getValidacion().setMensaje("Favor completar apellido materno");
                log.info("Favor completar apellido materno");
                return response;
            }
            if(usuario.getApellidoP()== null || usuario.getApellidoP().trim().equals("")){                
                response.getValidacion().setCodigoNegocio("5");
                response.getValidacion().setMensaje("Favor completar apellido paterno");
                log.info("Favor completar apellido paterno");
                return response;
            }   
            log.info("Validando datos dirección..."); 
            if(usuario.getDireccion() == null){                
                response.getValidacion().setCodigoNegocio("6");
                response.getValidacion().setMensaje("Favor completar los datos de dirección");
                log.info("Favor completar los datos de dirección");
                return response;
            }            
            if(usuario.getDireccion().getComuna()== null || usuario.getDireccion().getComuna().getIdComuna() == null){                
                response.getValidacion().setCodigoNegocio("7");
                response.getValidacion().setMensaje("Favor completar datos de comuna");
                log.info("Favor completar datos de comuna");
                return response;
            }            
            if(usuario.getDireccion().getComuna().getRegion()== null || usuario.getDireccion().getComuna().getRegion().getIdRegion() == null){                
                response.getValidacion().setCodigoNegocio("8");
                response.getValidacion().setMensaje("Favor completar región");
                log.info("Favor completar región");
                return response;
            } 
            if(usuario.getDireccion().getCalle() == null || usuario.getDireccion().getCalle().trim().equals("")){                
                response.getValidacion().setCodigoNegocio("9");
                response.getValidacion().setMensaje("Favor completar calle");
                log.info("Favor completar calle");
                return response;
            }
            if(usuario.getDireccion().getNumero()== null || usuario.getDireccion().getNumero().trim().equals("")){                
                response.getValidacion().setCodigoNegocio("10");
                response.getValidacion().setMensaje("Favor completar número");
                log.info("Favor completar calle");
                return response;
            }
            log.info("Validando datos contacto..."); 
            if(usuario.getContacto() == null){                
                response.getValidacion().setCodigoNegocio("11");
                response.getValidacion().setMensaje("Favor datos de contacto");
                log.info("Favor completar calle");
                return response;
            }             
            if(usuario.getContacto().getCelular() == null){                
                response.getValidacion().setCodigoNegocio("12");
                response.getValidacion().setMensaje("Favor completar celular");
                log.info("Favor completar celular");
                return response;
            } 
            if(usuario.getFechaNacimiento() == null){                
                response.getValidacion().setCodigoNegocio("13");
                response.getValidacion().setMensaje("Favor completar fecha de nacimiento");
                log.info("Favor completar fecha de nacimiento");
                return response;
            } 
            log.info("Datos de entrada ->{}",usuario.toString());
            response.getValidacion().setCodigoNegocio("0");
            response.getValidacion().setMensaje("Datos OK");
            log.info("Datos OK");
            
            
        } catch (Exception e) {
            response.setValidacion(new Validacion("1","1","Error al guardar datos de perfil"));
            log.error("Exception guardarDatosPerfil,",e);
        }
        log.info("fin");
        return response;
    }
    
    

    @Override
    public InfoDatosPerfilResponse guardarDatosPerfil(InfoDatosPerfilRequest request){
        InfoDatosPerfilResponse response = new InfoDatosPerfilResponse();
        response.setValidacion(new Validacion("0","1","Problemas al guardar datos de perfil"));
        log.info("inicio");
        try {
            
            if(request!=null && request.getIdUsuario()!=null){
                //log.info("datos de entrada ->{}",request.toString());
                String mensajeLog = "[idUsuario -> " + request.getIdUsuario() + "] ";
                
                if(request.getTenantId()!=null){
                    
                    ValidacionResponse val = this.validaDatosUsuario(request.getUsuario());
                    if(val!=null && val.getValidacion()!=null && val.getValidacion().getCodigo()!=null &&
                       val.getValidacion().getCodigo().equals("0") && val.getValidacion().getCodigoNegocio()!=null &&
                       val.getValidacion().getCodigoNegocio().equals("0")){
                        request.getUsuario().setIdUsuario(request.getIdUsuario());
                        log.info("buscando datos en empresa esquema ->{}",request.getTenantId());
                        empresaMapper.cambiarEsquema(request.getTenantId());
                        log.info("{} datos contacto->{}",mensajeLog,request.getUsuario().getContacto().toString());
                        if(request.getUsuario().getContacto().getIdContacto() == null){
                            log.info("{} insertando contacto->{}",mensajeLog);
                            Integer id = perfilMapper.getSeqIdContacto();
                            request.getUsuario().getContacto().setIdContacto(id);
                            log.info("{} id contacto->{}",mensajeLog,id);
                            perfilMapper.insertContacto(request.getUsuario().getContacto());
                        }
                        else{
                            log.info("{} actualizando contacto->{}",mensajeLog);
                            perfilMapper.updateContacto(request.getUsuario().getContacto());
                        }
                        log.info("{} datos dirección->{}",mensajeLog,request.getUsuario().getDireccion().toString());
                        if(request.getUsuario().getDireccion().getIdDireccion()== null){
                            log.info("{} insertando dirección->{}",mensajeLog);
                            Integer id = perfilMapper.getSeqIdDireccion();
                            request.getUsuario().getDireccion().setIdDireccion(id);
                            log.info("{} id dirección->{}",mensajeLog,id);
                            perfilMapper.insertDireccion(request.getUsuario().getDireccion());
                        }
                        else{
                            log.info("{} actualizando dirección...",mensajeLog);
                            perfilMapper.updateDireccion(request.getUsuario().getDireccion());
                        }
                        log.info("{} actualizando datos personales, usuario ->{}", mensajeLog,request.getUsuario().toString());
                        perfilMapper.updateDatosPerfil(request.getUsuario());                        
                        log.info("{} Datos de perfil actualizados correctamente...", mensajeLog);
                        response.getValidacion().setCodigoNegocio("0");
                        response.getValidacion().setMensaje("Datos de perfil actualizados correctamente...");
                    }
                    else{
                        response.setValidacion(val.getValidacion());
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
            response.setValidacion(new Validacion("1","1","Error al guardar datos de perfil"));
            log.error("Exception guardarDatosPerfil,",e);
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
                    log.info("{} Obteniendo datos perfil->{}", mensajeLog,response.getUsuario().toString());
                    if(response.getUsuario() != null){
                        if(response.getUsuario().getDireccion()!=null && response.getUsuario().getDireccion().getIdDireccion()!=null){
                            log.info("{} Obteniendo datos dirección(id:{})", mensajeLog,response.getUsuario().getDireccion().getIdDireccion());
                            response.getUsuario().setDireccion(perfilMapper.getDireccion(response.getUsuario().getDireccion().getIdDireccion()));
                        }
                        else{
                            log.info("{} Sin datos dirección!!", mensajeLog);
                        }
                        if(response.getUsuario().getContacto()!=null && response.getUsuario().getContacto().getIdContacto()!=null){
                            log.info("{} Obteniendo datos contacto(id:{})", mensajeLog,response.getUsuario().getContacto().getIdContacto());
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
