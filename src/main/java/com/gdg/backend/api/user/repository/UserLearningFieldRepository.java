package com.gdg.backend.api.user.repository;

import com.gdg.backend.api.user.domain.UserLearningField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLearningFieldRepository extends JpaRepository<UserLearningField, Long> {

    List<UserLearningField> findAllByUserId(Long userId);

    @Modifying
    @Query("delete from UserLearningField ulf where ulf.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query("select ulf from UserLearningField ulf where ulf.user.id = :userId")
    List<UserLearningField> findAllByUserIdQuery(@Param("userId") Long userId);
}
