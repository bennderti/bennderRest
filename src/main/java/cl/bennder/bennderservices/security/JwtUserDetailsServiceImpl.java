package cl.bennder.bennderservices.security;

import cl.bennder.bennderservices.mapper.EmpresaMapper;
import cl.bennder.bennderservices.mapper.UsuarioMapper;
import cl.bennder.bennderservices.multitenancy.TenantContext;
import cl.bennder.entitybennderwebrest.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private EmpresaMapper empresaMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String tenantId = TenantContext.getCurrentTenant();

        empresaMapper.cambiarEsquema(tenantId);
        Usuario usuario = usuarioMapper.getUsuarioByUsername(username, TenantContext.getCurrentTenant());

//        empresaMapper.cambiarEsquema(TenantContext.DEFAULT_TENANT);
        if (usuario == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(usuario);
        }

    }
}