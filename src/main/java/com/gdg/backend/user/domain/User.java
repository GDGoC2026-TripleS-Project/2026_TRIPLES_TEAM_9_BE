package com.gdg.backend.user.domain;

import com.gdg.backend.global.oauth.dto.UserInfoDto;
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

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider", nullable = false)
    private OauthProvider oauthProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private User(
            String email,
            String nickname,
            Role role,
            OauthProvider oauthProvider,
            String providerId,
            String profileImageUrl,
            UserStatus userStatus,
            String phoneNumber
    ) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.oauthProvider = oauthProvider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.userStatus = userStatus;
        this.phoneNumber = phoneNumber;
    }

    public static User fromOAuth(UserInfoDto info) {
        return User.builder()
                .oauthProvider(info.getProvider())
                .providerId(info.getProviderId())
                .email(info.getEmail())
                .nickname(info.getName())
//                .profileImageUrl(info.getProfileImageUrl())
                .role(Role.USER)
                .build();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updatePhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }

    public void updateUserStatus(UserStatus userStatus){
        this.userStatus = userStatus;
    }

    public void updateProfile(String nickname/*, String profileImageUrl*/) {
        if (nickname != null) {
            this.nickname = nickname;
        }
//        if (profileImageUrl != null) {
//            this.profileImageUrl = profileImageUrl;
//        }
    }

    public void updateRole(Role role) {
        this.role = role;
    }

//    public void updateProfileImage(String profileImageUrl) {
//        this.profileImageUrl = profileImageUrl;
//    }
}
