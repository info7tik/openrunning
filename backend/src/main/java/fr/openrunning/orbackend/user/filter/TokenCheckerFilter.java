package fr.openrunning.orbackend.user.filter;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import fr.openrunning.model.exception.OpenRunningException;
import fr.openrunning.orbackend.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenCheckerFilter implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(TokenCheckerFilter.class);
    private final List<String> methodWithAuthorization = Arrays.asList("DELETE", "GET", "PATCH", "POST", "PUT");
    @Autowired
    private UserService service;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws OpenRunningException {
        logger.info("Checking the token for " + request.getRequestURI());
        if (methodWithAuthorization.contains(request.getMethod())) {
            boolean isAuthenticated = false;
            try {
                String token = request.getHeader("Authorization");
                if (token != null) {
                    String[] tokenValue = token.split(" ");
                    if (tokenValue.length == 2) {
                        logger.info("successfully retrieved token: validating token...");
                        service.checkToken(tokenValue[1]);
                        isAuthenticated = true;
                    }
                } else {
                    logger.warn("No token found");
                }
            } catch (Exception e) {
                logger.error("error while checking the token", e);
            }
            if (!isAuthenticated) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
            return isAuthenticated;
        } else {
            return true;
        }
    }
}
