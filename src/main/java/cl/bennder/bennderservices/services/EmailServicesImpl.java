/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.services;

import cl.bennder.bennderservices.mapper.BeneficioMapper;
import cl.bennder.bennderservices.mapper.EmailMapper;
import cl.bennder.bennderservices.mapper.EmpresaMapper;
import cl.bennder.bennderservices.mapper.UsuarioMapper;
import cl.bennder.bennderservices.model.EmailTemplate;
import cl.bennder.bennderservices.model.ParametroSistema;
import cl.bennder.bennderservices.model.PlantillaCorreo;
import cl.bennder.bennderservices.model.SimpleMail;
import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.Validacion;
import cl.bennder.entitybennderwebrest.request.RecuperacionPasswordRequest;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;
import javax.annotation.Resource;
import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

/**
 *  EmailNotificationService para la notificación de correo electrónico 
 *  después de una presentación de podcast con éxito
 *  @author dyanez
 * 
 */
//fuente: http://www.technicalkeeda.com/spring-tutorials/spring-email-velocity-template-example,
//http://www.codingpedia.org/ama/how-to-compose-html-emails-in-java-with-spring-and-velocity/
@PropertySource("classpath:bennder.properties")
@Service
@Transactional
public class EmailServicesImpl implements EmailServices{
    
    private static final Logger log = LoggerFactory.getLogger(EmailServicesImpl.class);
    private static final String VELOCITY_BEANS_XML = "velocity-config.xml";
    private static final String TP_CORREO_SOPORTE = "CORREO_SOPORTE";
    private static final String C_CREDENCIALES = "CREDENCIALES";
    private static final String TP_BENNDER_USUARIO = "BENNDER_USUARIO";
    private static final String C_URL_PLATAFORMA = "URL_PLATAFORMA";
    private static final Integer ID_TEMPLATE_RECUPERACION_CORREO = 1;
    private static final Integer ID_TEMPLATE_ENVIO_LINK_CUPON = 2;
    private static final Integer ID_TEMPLATE_NOTIFICA_CANJE_BENEFICIO = 3;
    
    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @Autowired
    private EmailMapper emailMapper; 
    
    @Autowired
    private BeneficioMapper beneficioMapper; 
    
    @Autowired
    private EmpresaMapper empresaMapper;
    
    @Autowired
    private ParametroSistemaServices parametroSistemaServices;
    
    @Autowired
    private EncriptacionSpringService encriptacionSpringService;
    
    @Resource
    private Environment env;

    @Override
    public void sendEmail(String to) {
        log.info("inicio");
        try {
            log.info("to->{}",to);
            SimpleMail mail = new SimpleMail();
            mail.setMailFrom("soporte@bennder.cl");
            mail.setMailTo(to);
            mail.setMailSubject("Subject - Send Email using Spring Velocity Template");
//            mail.setTemplateName("emailtemplate.vm");
            log.info("ClassPathXmlApplicationContext...");
            ApplicationContext context = new ClassPathXmlApplicationContext("mail-config.xml");
            log.info("context.getBean...");
            Mailer mailer = (Mailer) context.getBean("mailer");
            if(mailer == null){
                log.info("Mailer is null...");
            }
            else{
                log.info("sendMail...");
                mailer.sendMail(mail,"recuperacion-password.vm");
            }
            
            
        } catch (Exception e) {
            log.error("Error en sendMail:",e);
        }
        log.info("fin");
    }

    @Override
    public void sendEmailMimeMessage(String to) {
        log.info("inicio");
        try {
            log.info("to->{}",to);
            SimpleMail mail = new SimpleMail();
            mail.setMailFrom("soporte@bennder.cl");
            mail.setMailTo(to);
            mail.setMailSubject("Subject - sendEmailMimeMessage");
            //mail.setTemplateName("recuperacion-password.vm");
            log.info("ClassPathXmlApplicationContext...");
            ApplicationContext context = new ClassPathXmlApplicationContext("mail-config.xml");
            log.info("context.getBean...");
            Mailer mailer = (Mailer) context.getBean("mailer");
            if(mailer == null){
                log.info("Mailer is null...");
            }
            else{
                log.info("sendMail...");
                mailer.sendMailMimeMessageHelper(mail,"recuperacion-password.vm");
            }
            
            
        } catch (Exception e) {
            log.error("Error en sendMail:",e);
        }
        log.info("fin");
    }

    @Override
    public ValidacionResponse notificarCanjeCuponBeneficio(Integer idUsuario, Integer idBeneficio) {
       log.info("inicio");
        ValidacionResponse response = new ValidacionResponse(new Validacion("0", "1", "Problemas al notificar canje de beneficio"));
        try {
            String correoUsuario = usuarioMapper.getUsuarioCorreo(idUsuario);
            if(correoUsuario != null){
                log.info("correo de usuario ->{} login en donde se enviará correo de validación/caje de cupon para usuario ->{}",correoUsuario,idUsuario);
                String mensajeLog = "[usuario -> "+idUsuario+"] ";                
                log.info("{} Obteniendo datos para formar correo y enviara usuario...",mensajeLog);
                log.info("obteniendo credenciales de correo de salida...");
                ParametroSistema paramCorreo = this.parametroSistemaServices.getDatosParametroSistema(TP_CORREO_SOPORTE, C_CREDENCIALES);
                if(paramCorreo != null){
                    EmailTemplate datosEmailTemplate = new EmailTemplate();
                    datosEmailTemplate.setMailFrom(paramCorreo.getValorA());
                    String passMailFrom = paramCorreo.getValorB();
                    String username = paramCorreo.getValorC();
                    //.- datos de plantilla
                    log.info("obteniendo datos de plantilla html...");
                    PlantillaCorreo plantillaCorreo = emailMapper.getDatosPlantillaCorreo(ID_TEMPLATE_NOTIFICA_CANJE_BENEFICIO);
                    if(plantillaCorreo != null){
                        log.info("datos plantilla correo ->{}",plantillaCorreo.toString());
                        Beneficio beneficio = beneficioMapper.getInfoGeneralBeneficio(idBeneficio);
                        log.info("{} nombreBeneficio ->{}",mensajeLog,beneficio.getTitulo());
                        datosEmailTemplate.setMailSubject(plantillaCorreo.getAsunto());
                        datosEmailTemplate.setNombreTemplate(plantillaCorreo.getNombre());
                        datosEmailTemplate.setMailTo(correoUsuario);

                       //Completando datos de contexto
                       log.info("{} Completando datos de contexto",mensajeLog);
                        VelocityContext velocityContext = new VelocityContext();
                        velocityContext.put("user", correoUsuario);
                        velocityContext.put("nombreBeneficio", beneficio.getTitulo());
                        velocityContext.put("proveedor", beneficio.getNombreProveedor());
                        datosEmailTemplate.setContext(velocityContext);
                        log.info("obteniendo beans de email...");
                        ApplicationContext context = new ClassPathXmlApplicationContext(VELOCITY_BEANS_XML);
                        Mailer mailer = (Mailer) context.getBean("mailer");
                        Validacion val = mailer.envioCorreoTemplate(datosEmailTemplate, passMailFrom,username); 
                        if(val!=null && "0".equals(val.getCodigo())&& "0".equals(val.getCodigoNegocio())){
                           val.setMensaje("Se ha enviado información de beneficio canjeado");
                           log.info("{} Se ha enviado información de beneficio canjeado a correo: {} ",mensajeLog,correoUsuario);
                        }
                        response.setValidacion(val);
                    }
                    else{
                        response.getValidacion().setCodigoNegocio("2");
                        response.getValidacion().setMensaje("Datos de plantilla de correo no encontrada");
                        log.info("{} Datos de plantilla de correo no encontrada",mensajeLog);
                    }
                }
                else{
                    response.getValidacion().setCodigoNegocio("1");
                    response.getValidacion().setMensaje("Datos de correo de salida no encontrados");
                    log.info("Datos de correo de salida no encontrados");
                }
                
            }
            else{
               response.getValidacion().setCodigoNegocio("1");
               response.getValidacion().setMensaje("No existe usuario correo utilizado para login");
               log.info("No existe usuario correo utilizado para login");
            }
            
            
        } catch (Exception e) {
            log.error("Error en notificarCanjeCuponBeneficio.",e);
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al notificar canje de beneficio");
        }
        log.info("fin");
        return response;
    }
    
    

    @Override
    public ValidacionResponse envioCorreoLinkCuponBeneficio(Integer idUsuario, Integer idBeneficio,String linkUrlDescargaCupon) {
        log.info("inicio");
        ValidacionResponse response = new ValidacionResponse(new Validacion("0", "1", "Problemas al recuperar contraseña"));
        try {
            String correoUsuario = usuarioMapper.getUsuarioCorreo(idUsuario);
            if(correoUsuario != null){
                log.info("correo de usuario ->{} login en donde se enviará link de generación de cupon para usuario ->{}",correoUsuario,idUsuario);
                String mensajeLog = "[usuario -> "+idUsuario+"] ";                
                log.info("{} Obteniendo datos para formar correo y enviara usuario...",mensajeLog);
                log.info("obteniendo credenciales de correo de salida...");
                ParametroSistema paramCorreo = this.parametroSistemaServices.getDatosParametroSistema(TP_CORREO_SOPORTE, C_CREDENCIALES);
                if(paramCorreo != null){
                    EmailTemplate datosEmailTemplate = new EmailTemplate();
                    datosEmailTemplate.setMailFrom(paramCorreo.getValorA());
                    String passMailFrom = paramCorreo.getValorB();
                    String username = paramCorreo.getValorC();
                    //.- datos de plantilla
                    log.info("obteniendo datos de plantilla html...");
                    PlantillaCorreo plantillaCorreo = emailMapper.getDatosPlantillaCorreo(ID_TEMPLATE_ENVIO_LINK_CUPON);
                    if(plantillaCorreo != null){
                        log.info("datos plantilla correo ->{}",plantillaCorreo.toString());
                        String nombreBeneficio = beneficioMapper.getTituloBeneficioAsuntoEnvioCorreo(idBeneficio);
                        log.info("{} nombreBeneficio ->{}",mensajeLog,nombreBeneficio);
                        datosEmailTemplate.setMailSubject(plantillaCorreo.getAsunto()+nombreBeneficio);
                        datosEmailTemplate.setNombreTemplate(plantillaCorreo.getNombre());
                        datosEmailTemplate.setMailTo(correoUsuario);

                       //Completando datos de contexto
                       log.info("{} Completando datos de contexto",mensajeLog);
                        VelocityContext velocityContext = new VelocityContext();
                        velocityContext.put("user", correoUsuario);
                        velocityContext.put("nombreBeneficio", nombreBeneficio);
                        velocityContext.put("linkUrlDownloadCupon", linkUrlDescargaCupon);
                        datosEmailTemplate.setContext(velocityContext);
                        log.info("obteniendo beans de email...");
                        ApplicationContext context = new ClassPathXmlApplicationContext(VELOCITY_BEANS_XML);
                        Mailer mailer = (Mailer) context.getBean("mailer");
                        Validacion val = mailer.envioCorreoTemplate(datosEmailTemplate, passMailFrom,username); 
                        if(val!=null && "0".equals(val.getCodigo())&& "0".equals(val.getCodigoNegocio())){
                           val.setMensaje("Se ha enviado información de beneficio obtenido a su correo: "+correoUsuario);
                           log.info("{} Se ha enviado información de beneficio obtenido a su correo: {} ",mensajeLog,correoUsuario);
                        }
                        response.setValidacion(val);
                    }
                    else{
                        response.getValidacion().setCodigoNegocio("2");
                        response.getValidacion().setMensaje("Datos de plantilla de correo no encontrada");
                        log.info("{} Datos de plantilla de correo no encontrada",mensajeLog);
                    }
                }
                else{
                    response.getValidacion().setCodigoNegocio("1");
                    response.getValidacion().setMensaje("Datos de correo de salida no encontrados");
                    log.info("Datos de correo de salida no encontrados");
                }
                
            }
            else{
               response.getValidacion().setCodigoNegocio("1");
               response.getValidacion().setMensaje("No existe usuario correo utilizado para login");
               log.info("No existe usuario correo utilizado para login");
            }
            
            
        } catch (Exception e) {
            log.error("Error en envioLinkCuponBeneficio.",e);
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al enviar link a usuario para cupon de beneficio");
        }
        log.info("fin");
        return response;
    }

    
    
    @Override
    public ValidacionResponse recuperacionPassword(RecuperacionPasswordRequest request) {
        log.info("inicio");
        ValidacionResponse response = new ValidacionResponse(new Validacion("0", "1", "Problemas al recuperar contraseña"));
        try {
            if(request != null && request.getUsuarioCorreo()!=null 
                && !request.getUsuarioCorreo().isEmpty() && !request.getUsuarioCorreo().trim().equals("")){
                log.info("Datos request ->{}",request.toString());
                String mensajeLog = "[usuario -> "+request.getUsuarioCorreo()+"] ";                
                if(request.getTenantId()!=null){
                    log.info("{} Esquema de usuario->{}",request.toString(),request.getTenantId());
                    empresaMapper.cambiarEsquema(request.getTenantId());
                    Integer existeCorreo = emailMapper.existeUsuarioCorreo(request.getUsuarioCorreo());
                    if(existeCorreo > 0){
                        if(existeCorreo.equals(1)){
                            //.- se busca contraseña
//                            log.info("{} obtiendo contraseña para comenzar con la creación de correo",existeCorreo);
//                            String password = emailMapper.getPasswordByUsuario(request.getUsuarioCorreo());
                            log.info("{} genenrando la contraseña temporal...",existeCorreo);
                            String passTemp = encriptacionSpringService.generarPasswordTemporal(60);
                            String passEncode = encriptacionSpringService.passEncoderGenerator(passTemp);
                            Integer idUsuario = usuarioMapper.getIdUsuarioByUsuarioCorreo(request.getUsuarioCorreo());
                            log.info("actualizando password temporal para usuario ->{}",idUsuario);
                            String urlBennderUsuario = env.getProperty("http")+request.getTenantId()+"."+env.getProperty("dns");
                            //String urlBennderUsuario = env.getProperty("server")+"/"+env.getProperty("dominio")+"/"+request.getTenantId()+"/index.html";
                            log.info("{} urlBennderUsuario ->{}",mensajeLog,urlBennderUsuario);
                            usuarioMapper.updatePassword(passEncode, idUsuario, true);
                           Validacion v = this.completarEnviarCorreoPassWord(passTemp, request.getUsuarioCorreo(),urlBennderUsuario);
                            response.setValidacion(v);
                        }
                        else{
                            response.getValidacion().setCodigoNegocio("3");
                            response.getValidacion().setMensaje("Existe mas de un usuario registrado con su correo, favor contactar a soporte");
                            log.info("{} Existe mas de un usuario registrado con su correo, favor contactar a soporte",mensajeLog);
                        }
                    }
                    else{
                        response.getValidacion().setCodigoNegocio("2");
                        response.getValidacion().setMensaje("No existe usuario registrado");
                        log.info("{} No existe usuario registrado",mensajeLog);
                    }
                }
                else{
                        response.getValidacion().setCodigoNegocio("4");
                        response.getValidacion().setMensaje("Dominio no identificado para usuario");
                        log.info("{} Dominio no identificado para usuario",mensajeLog);
                }

            }
            else{
               response.getValidacion().setCodigoNegocio("1");
               response.getValidacion().setMensaje("Datos de entrada no válidos");
               log.info("Datos de entrada no válidos");
            }
            
        } catch (Exception e) {
            log.error("Error en recuperacionPassword.",e);
            response.getValidacion().setCodigo("1");
            response.getValidacion().setCodigoNegocio("1");
            response.getValidacion().setMensaje("Error al recuperar contraseña");
        }
        log.info("fin");
        return response;
    }

    @Override
    public Validacion completarEnviarCorreoPassWord(String password, String usuario, String urlBennderUsuario) {
        Validacion response = new Validacion("0", "1", "Problemas al completar correo");
        log.info("inicio");
        try {
            //.- obteniendo credenciales correo soporte/salida
            //.- obteniendo beans para creación de correo
            //.- completar template de correo
            //.- emviar correo
            log.info("obteniendo credenciales de correo de salida (soporte)");
            ParametroSistema paramCorreo = this.parametroSistemaServices.getDatosParametroSistema(TP_CORREO_SOPORTE, C_CREDENCIALES);
            if(paramCorreo != null){
                EmailTemplate datosEmailTemplate = new EmailTemplate();
                datosEmailTemplate.setMailFrom(paramCorreo.getValorA());
                String passMailFrom = paramCorreo.getValorB();
                String username = paramCorreo.getValorC();
                //.- datos de plantilla
                log.info("obteniendo datos de plantilla html...");
                PlantillaCorreo plantillaCorreo = emailMapper.getDatosPlantillaCorreo(ID_TEMPLATE_RECUPERACION_CORREO);
                if(plantillaCorreo != null){
                    log.info("datos plantilla correo ->{}",plantillaCorreo.toString());
                    datosEmailTemplate.setMailSubject(plantillaCorreo.getAsunto());
                    datosEmailTemplate.setNombreTemplate(plantillaCorreo.getNombre());
                    datosEmailTemplate.setMailTo(usuario);
                    
//                    ParametroSistema paramUrlBennder = this.parametroSistemaServices.getDatosParametroSistema(TP_BENNDER_USUARIO, C_URL_PLATAFORMA);
//                    if(paramUrlBennder != null){
                        //log.info("Url acceso plataforma bennder ->{}, para  usuario->{}",paramUrlBennder.getValorA(),usuario);
                       //Completando datos de contexto
                       log.info("Completando datos de contexto de template...");
                        VelocityContext velocityContext = new VelocityContext();
                        velocityContext.put("user", usuario);
                        velocityContext.put("password", password);
                        velocityContext.put("urlBennderUsuario", urlBennderUsuario);

                        datosEmailTemplate.setContext(velocityContext);
                        log.info("obteniendo beans de email...");
                        ApplicationContext context = new ClassPathXmlApplicationContext(VELOCITY_BEANS_XML);
                        Mailer mailer = (Mailer) context.getBean("mailer");
                        response = mailer.envioCorreoTemplate(datosEmailTemplate, passMailFrom,username); 
//                    }
//                    else{  
//                        response.setCodigoNegocio("3");
//                        response.setMensaje("Sin plataforma configurada para usuario, favor contactar a soporte.");
//                        log.info("Sin plataforma configurada para usuario->{}",usuario);
//                    }
                  
                }
                else{
                    response.setCodigoNegocio("2");
                    response.setMensaje("Datos de plantilla de correo no encontrada");
                    log.info("Datos de plantilla de correo no encontrada");
                }
            }
            else{
                response.setCodigoNegocio("1");
                response.setMensaje("Datos de correo de salida no encontrados");
                log.info("Datos de correo de salida no encontrados");
            }
            
        } catch (Exception e) {
            log.error("Error en completarEnviarCorreoPassWord.",e);
            response.setCodigo("1");
            response.setCodigoNegocio("1");
            response.setMensaje("Error al recuperar contraseña");
        }
        log.info("fin");
        return response;
    }
    
    
    
}
