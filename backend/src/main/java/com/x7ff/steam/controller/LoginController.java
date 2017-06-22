package com.x7ff.steam.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenID4JavaConsumer;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.openid.OpenIDConsumerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public final class LoginController {
    private static final String PRIMARY_AUTH_ENDPOINT = "/auth/";
    private static final String STEAM_AUTH_ENDPOINT = PRIMARY_AUTH_ENDPOINT + "steam/";
    private static final String FINISHED_STEAM_AUTH_ENDPOINT = PRIMARY_AUTH_ENDPOINT + "steam/finish/";
    private static final String LOGOUT_ENDPOINT = PRIMARY_AUTH_ENDPOINT + "logout/";

    @Inject
    private OpenID4JavaConsumer openID4JavaConsumer;

    @Inject
    AuthenticationManager authenticationManager;

    @RequestMapping(PRIMARY_AUTH_ENDPOINT)
    public String login() {
        return "login";
    }

    @RequestMapping(STEAM_AUTH_ENDPOINT)
    public String steamLogin(HttpServletRequest request) {
        String requestedUrl = request.getRequestURL().toString();
        String baseUrl = requestedUrl.substring(0, requestedUrl.indexOf(STEAM_AUTH_ENDPOINT));
        String returnUrl = baseUrl + FINISHED_STEAM_AUTH_ENDPOINT;
        try {
            String redirectUrl = openID4JavaConsumer.beginConsumption(request, "https://steamcommunity.com/openid", returnUrl, baseUrl);
            return "redirect:" + redirectUrl;
        } catch (OpenIDConsumerException e) {
            e.printStackTrace();
        }
        return "redirect:" + PRIMARY_AUTH_ENDPOINT;
    }

    @RequestMapping(FINISHED_STEAM_AUTH_ENDPOINT)
    public String steamAuth(HttpServletRequest request) {
        OpenIDAuthenticationToken token = null;
        try {
            token = openID4JavaConsumer.endConsumption(request);
        } catch (OpenIDConsumerException e) {
            e.printStackTrace();
        }
        if (token == null || token.getStatus() != OpenIDAuthenticationStatus.SUCCESS) {
            return "redirect:" + PRIMARY_AUTH_ENDPOINT;
        }
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + PRIMARY_AUTH_ENDPOINT;
        }
        return "redirect:/";
    }

    @RequestMapping(LOGOUT_ENDPOINT)
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "redirect:/";
        }
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }

}