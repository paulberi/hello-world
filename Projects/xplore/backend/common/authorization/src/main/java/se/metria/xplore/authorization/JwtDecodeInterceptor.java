package se.metria.xplore.authorization;

import com.auth0.jwt.JWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties(AuthorizationProperties.class)
public class JwtDecodeInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(JwtDecodeInterceptor.class);

    public JwtDecodeInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null)
        {
            String token = authHeader.substring("Bearer ".length());
            request.getSession().setAttribute("jwt", JWT.decode(token));
        }
        else
        {
            logger.debug("No Authorization header present, just passing through");
        }

        return true;
    }
}
