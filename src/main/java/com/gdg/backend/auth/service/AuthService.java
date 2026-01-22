package com.gdg.backend.auth.service;

import com.gdg.backend.auth.dto.AuthLoginRequestDto;
import com.gdg.backend.auth.dto.AuthOnboardingRequestDto;
import com.gdg.backend.global.jwt.TokenProvider;
import com.gdg.backend.global.security.SignupPrincipal;
import com.gdg.backend.user.domain.*;
import com.gdg.backend.user.dto.TokenResponseDto;
import com.gdg.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public TokenResponseDto login(AuthLoginRequestDto request) {

        SignupPrincipal principal =
                tokenProvider.parseSignupToken(request.getIdToken());

        User user = userRepository
                .findByOauthProviderAndProviderId(
                        principal.provider(),
                        principal.providerId()
                )
                .orElseGet(() -> signup(principal));

        return issueToken(user);
    }

    public TokenResponseDto onboarding(
            AuthOnboardingRequestDto request
    ) {
        SignupPrincipal principal = parseSignupToken(request.getIdToken());

        User user = findPendingUser(principal);

        applyOnboardingInfo(user, request, principal);


        activateUser(user);

        return issueToken(user);
    }

    private User signup(SignupPrincipal principal) {

        User user = User.builder()
                .oauthProvider(principal.provider())
                .providerId(principal.providerId())
                .email(principal.email())
                .nickname(generateTempNickname(principal))
                .role(Role.USER)
                .userStatus(UserStatus.PENDING)
                .build();

//        if (isSuperAdmin(principal.provider(), principal.providerId())) {
//            user.updateRole(Role.SUPER_ADMIN);
//        }

        return userRepository.save(user);
    }

    private TokenResponseDto issueToken(User user) {

        String accessToken = tokenProvider.accessToken(user);
        String refreshToken = tokenProvider.refreshToken(user);

        user.updateRefreshToken(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .oauthProvider(user.getOauthProvider())
                .role(user.getRole())
                .newUser(user.getUserStatus() == UserStatus.PENDING)
                .build();
    }

    private SignupPrincipal parseSignupToken(String idToken) {
        return tokenProvider.parseSignupToken(idToken);
    }

    private User findPendingUser(SignupPrincipal principal) {
        User user = userRepository
                .findByOauthProviderAndProviderId(
                        principal.provider(),
                        principal.providerId()
                )
                .orElseThrow(() ->
                        new IllegalStateException("존재하지 않는 사용자입니다.")
                );

        if (user.getUserStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("온보딩 대상 사용자가 아닙니다.");
        }

        return user;
    }

    private void applyOnboardingInfo(
            User user,
            AuthOnboardingRequestDto request,
            SignupPrincipal principal
    ) {
        user.updateNickname(request.getNickname());

    }

    private void activateUser(User user) {
        user.updateUserStatus(UserStatus.ACTIVE);
    }

    private boolean hasImage(MultipartFile image) {
        return image != null && !image.isEmpty();
    }

    private String generateTempNickname(SignupPrincipal principal) {
        return "user_" +
                principal.provider().name().toLowerCase() +
                "_" +
                principal.providerId();
    }

//    private boolean isSuperAdmin(OauthProvider provider, String providerId) {
//        return provider == superAdminProperties.getOauthProvider()
//                && providerId.equals(superAdminProperties.getProviderId());
//    }
}
