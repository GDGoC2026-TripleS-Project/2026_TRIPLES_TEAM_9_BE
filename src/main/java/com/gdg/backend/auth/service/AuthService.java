package com.gdg.backend.auth.service;

import com.gdg.backend.auth.config.SuperAdminProperties;
import com.gdg.backend.auth.dto.AuthIssueResult;
import com.gdg.backend.auth.dto.AuthLoginRequestDto;
import com.gdg.backend.auth.dto.AuthOnboardingRequestDto;
import com.gdg.backend.auth.dto.IssuedTokens;
import com.gdg.backend.global.jwt.TokenProvider;
import com.gdg.backend.global.security.SignupPrincipal;
import com.gdg.backend.user.domain.*;
import com.gdg.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final SuperAdminProperties superAdminProperties;

    /**
     * 로그인 (기존 회원 or 신규 가입)
     */
    @Transactional
    public AuthIssueResult login(AuthLoginRequestDto request) {
        SignupPrincipal principal = tokenProvider.parseSignupToken(request.getAuthToken());

        User user = userRepository
                .findByOauthProviderAndProviderId(principal.oauthProvider(), principal.providerId())
                .orElseGet(() -> signup(principal));

        IssuedTokens tokens = issueTokens(user);
        return toIssueResult(user, tokens);
    }

    /**
     * 온보딩 완료
     */
    @Transactional
    public AuthIssueResult onboarding(AuthOnboardingRequestDto request) {
        SignupPrincipal principal = tokenProvider.parseSignupToken(request.getAuthToken());

        User user = findPendingUser(principal);

        applyOnboardingInfo(user, request);
        activateUser(user);

        IssuedTokens tokens = issueTokens(user);
        return toIssueResult(user, tokens);
    }

    /**
     * access 재발급 (refresh는 httpOnly cookie로 들어옴)
     */
    @Transactional
    public AuthIssueResult refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalStateException("Refresh token이 없습니다.");
        }

        Long userId = tokenProvider.getSubjectAsUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new IllegalStateException("Refresh token이 유효하지 않습니다.");
        }

        IssuedTokens newTokens = issueTokens(user);
        return toIssueResult(user, newTokens);
    }

    private IssuedTokens issueTokens(User user) {
        String accessToken = tokenProvider.accessToken(user);
        String refreshToken = tokenProvider.refreshToken(user);

        user.updateRefreshToken(refreshToken);

        return new IssuedTokens(accessToken, refreshToken);
    }

    private AuthIssueResult toIssueResult(User user, IssuedTokens tokens) {
        return new AuthIssueResult(
                tokens,
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getOauthProvider(),
                user.getRole(),
                user.getUserStatus() == UserStatus.PENDING
        );
    }

    private User signup(SignupPrincipal principal) {
        Role role = Role.USER;

        if (isSuperAdmin(principal)) {
            role = Role.SUPER_ADMIN;
        }

        User user = User.builder()
                .oauthProvider(principal.oauthProvider())
                .providerId(principal.providerId())
                .email(principal.email())
                .nickname(generateTempNickname(principal))
                .role(role)
                .userStatus(UserStatus.PENDING)
                .build();

        return userRepository.save(user);
    }

    private boolean isSuperAdmin(SignupPrincipal principal) {
        if (superAdminProperties == null) return false;

        if (superAdminProperties.getOauthProvider() == null) return false;
        if (superAdminProperties.getProviderId() == null || superAdminProperties.getProviderId().isBlank()) return false;
        if (superAdminProperties.getEmail() == null || superAdminProperties.getEmail().isBlank()) return false;

        return principal.oauthProvider() == superAdminProperties.getOauthProvider()
                && principal.providerId().equals(superAdminProperties.getProviderId())
                && principal.email().equalsIgnoreCase(superAdminProperties.getEmail());
    }

    private User findPendingUser(SignupPrincipal principal) {
        User user = userRepository
                .findByOauthProviderAndProviderId(principal.oauthProvider(), principal.providerId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        if (user.getUserStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("온보딩 대상 사용자가 아닙니다.");
        }

        return user;
    }

    private void applyOnboardingInfo(User user, AuthOnboardingRequestDto request) {
        user.updateNickname(request.getNickname());
    }

    private void activateUser(User user) {
        user.updateUserStatus(UserStatus.ACTIVE);
    }

    private String generateTempNickname(SignupPrincipal principal) {
        return "user_" +
                principal.oauthProvider().name().toLowerCase() +
                "_" +
                principal.providerId();
    }
}
