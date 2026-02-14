package com.gdg.backend.api.auth.service;

import com.gdg.backend.api.auth.dto.AuthIssueResult;
import com.gdg.backend.api.auth.dto.AuthLoginRequestDto;
import com.gdg.backend.api.auth.dto.AuthOnboardingRequestDto;
import com.gdg.backend.api.auth.dto.IssuedTokens;
import com.gdg.backend.api.auth.exception.AuthErrorCode;
import com.gdg.backend.api.auth.exception.AuthException;
import com.gdg.backend.api.auth.nickname.NicknameGenerator;
import com.gdg.backend.api.auth.policy.SuperAdminPolicy;
import com.gdg.backend.api.global.jwt.TokenProvider;
import com.gdg.backend.api.global.security.SignupPrincipal;
import com.gdg.backend.api.user.account.repository.UserRepository;
import com.gdg.backend.api.user.domain.Role;
import com.gdg.backend.api.user.domain.User;
import com.gdg.backend.api.user.domain.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final SuperAdminPolicy superAdminPolicy;
    private final NicknameGenerator nicknameGenerator;
    private final OnboardingService onboardingService;

    /**
     * 로그인 (기존 회원 or 신규 가입)
     */
    @Transactional
    public AuthIssueResult login(AuthLoginRequestDto request) {
        SignupPrincipal principal = tokenProvider.parseSignupToken(request.getAuthToken());

        User user = userRepository
                .findByOauthProviderAndProviderId(principal.oauthProvider(), principal.providerId())
                .orElseGet(() -> signup(principal));

        if (user.getRole() != Role.SUPER_ADMIN && superAdminPolicy.isSuperAdmin(principal)) {
            user.updateRole(Role.SUPER_ADMIN);
        }

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

        onboardingService.apply(user, request);
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
            log.warn("refresh failed: missing refresh token");
            throw new AuthException(AuthErrorCode.NO_REFRESH_TOKEN);
        }

        Long userId = tokenProvider.getSubjectAsUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            log.warn("refresh failed: invalid refresh token for userId={}", userId);
            throw new AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN);
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

        if (superAdminPolicy.isSuperAdmin(principal)) {
            role = Role.SUPER_ADMIN;
        }

        User user = User.builder()
                .oauthProvider(principal.oauthProvider())
                .providerId(principal.providerId())
                .email(principal.email())
                .nickname(nicknameGenerator.generate(principal))
                .role(role)
                .userStatus(UserStatus.PENDING)
                .build();

        return userRepository.save(user);
    }

    private User findPendingUser(SignupPrincipal principal) {
        User user = userRepository
                .findByOauthProviderAndProviderId(principal.oauthProvider(), principal.providerId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND_FOR_ONBOARDING));

        if (user.getUserStatus() != UserStatus.PENDING) {
            throw new AuthException(AuthErrorCode.NOT_PENDING_USER);
        }

        if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(principal.email())) {
            throw new AuthException(AuthErrorCode.TOKEN_USER_MISMATCH);
        }

        return user;
    }

    private void activateUser(User user) {
        user.updateUserStatus(UserStatus.ACTIVE);
    }
}
