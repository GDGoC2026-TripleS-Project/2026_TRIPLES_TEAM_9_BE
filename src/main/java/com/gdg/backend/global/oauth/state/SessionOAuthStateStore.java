package com.gdg.backend.global.oauth.state;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionOAuthStateStore implements OAuthStateStore {

    private static final String KEY = "OAUTH_NAVER_STATE";

    private final HttpSession session;

    public SessionOAuthStateStore(HttpSession session) {
        this.session = session;
    }

    @Override
    public void save(String state) {
        session.setAttribute(KEY, state);
    }

    @Override
    public boolean consume(String state) {
        Object saved = session.getAttribute(KEY);
        session.removeAttribute(KEY); // 1회용
        return saved != null && saved.equals(state);
    }
}
