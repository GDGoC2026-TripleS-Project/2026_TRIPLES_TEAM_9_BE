package com.gdg.backend.api.global.cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public void addHttpOnlyCookie(HttpServletResponse response,
                                  String name,
                                  String value,
                                  long maxAgeSeconds,
                                  boolean secure,
                                  String sameSite,
                                  String path,
                                  String domain){

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .path(path)
                .maxAge(maxAgeSeconds)
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        ResponseCookie cookie = builder.build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response,
                                  String name,
                                  boolean secure,
                                  String sameSite,
                                  String path,
                                  String domain){

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secure)
                .path(path)
                .maxAge(0)
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        ResponseCookie cookie = builder.build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
