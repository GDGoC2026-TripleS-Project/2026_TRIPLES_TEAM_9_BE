package com.gdg.backend.api.user.domain;

import com.gdg.backend.api.global.oauth.dto.UserInfoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_oauth_provider_provider_id",
                        columnNames = {"oauth_provider", "provider_id"}
                )
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", nullable = false)
    private OauthProvider oauthProvider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private User(
            String email,
            String nickname,
            Role role,
            OauthProvider oauthProvider,
            String providerId,
            UserStatus userStatus
    ) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.oauthProvider = oauthProvider;
        this.providerId = providerId;
        this.userStatus = userStatus;
    }

    public static User fromOAuth(UserInfoDto info) {
        String nickname = (info.getName() == null || info.getName().isBlank())
                ? "USER_" + info.getProvider() + "_" + info.getProviderId()
                : info.getName();

        return User.builder()
                .email(info.getEmail())
                .nickname(nickname)
                .role(Role.USER)
                .providerId(info.getProviderId())
                .oauthProvider(info.getProvider())
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
    }

    public void updateUserStatus(UserStatus userStatus) {
        if (userStatus != null) {
            this.userStatus = userStatus;
        }
    }

    public void updateRole(Role role) {
        if (role != null) {
            this.role = role;
        }
    }

    public void updateEmail(String email) {
        // 필요할 때(추가정보 입력)만 세팅하도록 열어둠
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
    }
}
