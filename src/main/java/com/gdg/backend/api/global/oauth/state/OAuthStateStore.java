package com.gdg.backend.api.global.oauth.state;

public interface OAuthStateStore {
    void save(String state);
    boolean consume(String state); // 존재하면 삭제하고 true
}
