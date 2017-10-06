/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.controller;

import cl.bennder.bennderservices.multitenancy.TenantContext;
import cl.bennder.bennderservices.security.JwtTokenUtil;
import cl.bennder.bennderservices.security.JwtUser;
import cl.bennder.bennderservices.services.UsuarioServices;
import cl.bennder.entitybennderwebrest.model.Validacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import cl.bennder.bennderservices.services.EmailServices;
import cl.bennder.bennderservices.services.HomeServices;
import cl.bennder.entitybennderwebrest.request.CambioPasswordRequest;
import cl.bennder.entitybennderwebrest.request.CargarHomeRequest;
import cl.bennder.entitybennderwebrest.request.LoginRequest;
import cl.bennder.entitybennderwebrest.request.RecuperacionPasswordRequest;
import cl.bennder.entitybennderwebrest.response.CambioPasswordResponse;
import cl.bennder.entitybennderwebrest.response.CargarHomeResponse;
import cl.bennder.entitybennderwebrest.response.LoginResponse;
import cl.bennder.entitybennderwebrest.response.ValidacionResponse;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dyanez
 */
@RestController
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UsuarioServices usuarioServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailServices emailServices;
    
    @Autowired
    private HomeServices homeServices;

    /*//.- login
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public @ResponseBody
    LoginResponse login(@RequestBody LoginRequest request) {
        log.info("[login] - inicio ");
        LoginResponse response = usuarioServices.validacionUsuario(request);
        log.info("response ->{}", response.toString());
        log.info("[login] - fin ");
        
        return response;
    }*/
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws AuthenticationException {

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setValidacion(new Validacion("0","1","Usuario no encontrado"));
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUser(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(authenticationRequest.getUser());
        final String token = jwtTokenUtil.generateToken(userDetails);
        authenticationRequest.setPassword(userDetails.getPassword());
        //authenticationRequest.setPassword(userDetails.getPassword());
        log.info("[login] - validando usuario");
        log.info("[login] - TenantContext.getCurrentTenant()->{}",TenantContext.getCurrentTenant());
        loginResponse = usuarioServices.validacionUsuario(authenticationRequest);

        // Return the token
        //loginResponse.setValidacion(new Validacion("0","0","login exitoso"));
        loginResponse.setToken(token);
        loginResponse.setIdEstadoUsuario(userDetails.getIdEstado());
        log.info("[login] - fin ");
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Utilizado para testear email utilizando velocity como template
     * @param  to Destinatario 
     */
    @RequestMapping(value = "velocity/emailMimeMessage", method = RequestMethod.POST)
    public void velocityEmailMimeMessage(@RequestParam("to") String to) {
        log.info("[velocity] - inicio ");
        emailServices.sendEmailMimeMessage(to);
        //emailByVelocity.sendEmail(to);
        log.info("[velocity] - fin ");
    }
     /***
     * Servicio utilizado para enviar contraseña a correo de usuario
     * @param  request Usuario correo destinatario 
     * @return 
     */
    @RequestMapping(value = "mail/recuperacionPassword", method = RequestMethod.POST)
    public ValidacionResponse recuperacionPassword(@RequestBody RecuperacionPasswordRequest request) {
        log.info("[mail/recuperacionPassword] - inicio ");
        ValidacionResponse response = emailServices.recuperacionPassword(request);
        log.info("[mail/recuperacionPassword] - fin ");
        return response;
    }
    
    /**
     * Método para obtener toda la informaciòn a cargar en el home
     * MG - 9.4.2017
     * @param request usuario
     * @return response lista de categorías, beneficios preferenciales, ùltimos vistos, tendencias, publicidad
     */
    @RequestMapping(value = "home/cargarHome", method = RequestMethod.POST)
    public @ResponseBody
    CargarHomeResponse cargarHome(@RequestBody CargarHomeRequest cargarHomeRequest, HttpServletRequest request) {
        log.info("[home/cargarHome] - inicio ");

        //obteniendo idUsuario desde token
        cargarHomeRequest.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(request));
        log.debug("idUsuario -> " + cargarHomeRequest.getIdUsuario());

        CargarHomeResponse response =  homeServices.cargarHome(cargarHomeRequest);
        
//        log.info("response ->{}", response.toString());
        log.info("[home/cargarHome] - fin ");
        
        return response;
    }
    
    @RequestMapping(value = "login/cambioPassword", method = RequestMethod.POST)
    public CambioPasswordResponse cambioPassword(@RequestBody CambioPasswordRequest request, HttpServletRequest req) {
        log.info("[login/cambioPassword] - inicio ");
        //request.setIdUsuario(jwtTokenUtil.getIdUsuarioDesdeRequest(req));
        CambioPasswordResponse response = usuarioServices.cambioPassword(request);
        log.info("[login/cambioPassword] - fin ");
        return response;
    }
}
