package com.gdg.backend.user.repository;

import com.gdg.backend.user.domain.OauthProvider;
import com.gdg.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthProviderAndProviderId(OauthProvider oauthProvider, String providerId);
    boolean existsByOauthProviderAndProviderId(OauthProvider oauthProvider, String providerId);
}
