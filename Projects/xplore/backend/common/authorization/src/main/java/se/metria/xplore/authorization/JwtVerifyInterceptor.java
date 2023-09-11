package se.metria.xplore.authorization;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;

@Component
@EnableConfigurationProperties(AuthorizationProperties.class)
public class JwtVerifyInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(JwtVerifyInterceptor.class);
    private JwtVerifier jwtVerifier;
    private AuthorizationProperties authorizationProperties;

    public JwtVerifyInterceptor(AuthorizationProperties authorizationProperties) throws MalformedURLException {
        this.authorizationProperties = authorizationProperties;
        if (!this.authorizationProperties.isDisabled()) {
            this.jwtVerifier = new JwtVerifier(authorizationProperties.getCertsUrl());
        }
        else {
            logger.debug("Authorization is disabled; will not verify anything");
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (authorizationProperties.isDisabled()) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            logger.error("No Authorization header present; request not allowed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        DecodedJWT jwt;

        try {
            String token = authHeader.substring("Bearer ".length());
            jwt = jwtVerifier.verify(token);
        } catch (Exception e) {
            logger.error("Error while verifying token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (logger.isDebugEnabled()) {
            String name = jwt.getClaim("name").asString();
            String email = jwt.getClaim("email").asString();
            logger.debug("Token verified: " + name + " (" + email + ")");
        }

        return true;
    }
}
