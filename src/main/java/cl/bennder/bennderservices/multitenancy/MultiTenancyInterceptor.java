package cl.bennder.bennderservices.multitenancy;

import cl.bennder.bennderservices.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${tenant.id}")
    private String tenant;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {

        String token = req.getHeader(this.tokenHeader);
        String tenantId;
        if (StringUtils.isEmpty(token)){
            tenantId = req.getHeader(tenant);
        }
        else {
            tenantId = jwtTokenUtil.getTenantFromToken(token);
        }

        if(StringUtils.isEmpty(tenantId)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.getWriter().write("{\"error\": \"No tenant supplied\"}");
            res.getWriter().flush();

            return false;
        }

        TenantContext.setCurrentTenant(tenantId);
        return true;

    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        TenantContext.clear();
    }
}