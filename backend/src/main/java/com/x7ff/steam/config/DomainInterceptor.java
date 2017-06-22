package com.x7ff.steam.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public final class DomainInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String domain = request.getRequestURL().toString();
        domain = domain.substring(0, domain.lastIndexOf("/") + 1);

        SecurityContext context = SecurityContextHolder.getContext();
        AbstractAuthenticationToken authenticationToken = (AbstractAuthenticationToken) context.getAuthentication();
        if (authenticationToken != null) {
            authenticationToken.setDetails(domain);
        }
        return true;
    }

}