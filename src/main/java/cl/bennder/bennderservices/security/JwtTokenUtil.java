package cl.bennder.bennderservices.security;

import cl.bennder.bennderservices.multitenancy.TenantContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_ID_USUARIO = "idUsuario";
    static final String CLAIM_KEY_AUTHORITIES = "authorities";
    static final String CLAIM_KEY_USER_ENABLED = "usuarioHabilitado";
    static final String CLAIM_KEY_TENANT_ID = "tenantId";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ID_USUARIO, ((JwtUser) userDetails).getId());
        claims.put(CLAIM_KEY_AUTHORITIES, ((JwtUser) userDetails).getAuthorities());
        claims.put(CLAIM_KEY_USER_ENABLED, userDetails.isEnabled());
        claims.put(CLAIM_KEY_TENANT_ID, TenantContext.getCurrentTenant());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, String usernameToken) {
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(usernameToken)
                        && !isTokenExpired(token));
    }

    public Integer getIdUsuarioFromToken(String token) {
        Integer idUsuario;
        try {
            final Claims claims = getClaimsFromToken(token);
            idUsuario = (Integer) claims.get(CLAIM_KEY_ID_USUARIO);
        } catch (Exception e) {
            idUsuario = null;
        }
        return idUsuario;
    }

    /**
     * Metodo que obtiene idUsuario desde el token seteado en el Header del request
     * @param request
     * @return idUsuario
     */
    public Integer getIdUsuarioDesdeRequest(HttpServletRequest request){
        return getIdUsuarioFromToken(request.getHeader(this.tokenHeader));
    }

    public UserDetails obtenerUserDetailsDesdeToken(String token){
        UserDetails user;
        try {
            final Claims claims = getClaimsFromToken(token);
            Integer id = (Integer) claims.get(CLAIM_KEY_ID_USUARIO);
            String username = (String) claims.get(CLAIM_KEY_USERNAME);
            Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) claims.get(CLAIM_KEY_AUTHORITIES);
            Boolean enabled = (Boolean) claims.get(CLAIM_KEY_USER_ENABLED);

            user = JwtUserFactory.create(id, username, authorities, enabled);
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    public String getTenantFromToken(String token) {

        String tenant;
        try {
            final Claims claims = getClaimsFromToken(token);
            tenant = (String) claims.get(CLAIM_KEY_TENANT_ID);
        } catch (Exception e) {
            tenant = null;
        }
        return tenant;
    }
}