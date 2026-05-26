package com.skywill.aozoralibrarian.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
public class SessionService {

    private static final String COOKIE_NAME = "AOZORA_SESSION_ID";
    private static final int COOKIE_MAX_AGE_DAYS = 30;

    /* CookieからセッションIDを取得、なければ新規発行してレスポンスにセット */

    public String resolveSessionId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> COOKIE_NAME.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElseGet(() -> issueNewSessionId(response));
        }
        return issueNewSessionId(response);
    }

    public String getSessionIdIfExists(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
    if (cookies == null){
        return null;
    }
    return Arrays.stream(cookies)
            .filter(c -> COOKIE_NAME.equals(c.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
    }

    public String issueNewSessionId(HttpServletResponse response) {
        String newSessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(COOKIE_NAME, newSessionId);
        cookie.setPath("/");
        cookie.setMaxAge((COOKIE_MAX_AGE_DAYS * 24 * 60 * 60));
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return newSessionId;
    }
}