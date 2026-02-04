package com.gdg.backend.api.user.profile.repository;

import com.gdg.backend.api.user.profile.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
