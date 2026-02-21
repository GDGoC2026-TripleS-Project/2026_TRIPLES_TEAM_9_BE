package com.gdg.backend.api.goal.repository;

import com.gdg.backend.api.goal.domain.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    @Query(
            value = """
                    select distinct g
                    from Goal g
                    left join g.tasks t
                    where g.user.id = :userId
                      and (
                        :keyword is null
                        or lower(g.title) like lower(concat('%', :keyword, '%'))
                        or lower(t.content) like lower(concat('%', :keyword, '%'))
                      )
                    """,
            countQuery = """
                    select count(distinct g)
                    from Goal g
                    left join g.tasks t
                    where g.user.id = :userId
                      and (
                        :keyword is null
                        or lower(g.title) like lower(concat('%', :keyword, '%'))
                        or lower(t.content) like lower(concat('%', :keyword, '%'))
                      )
                    """
    )
    Page<Goal> searchGoals(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Modifying
    @Query("delete from Goal g where g.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    Optional<Goal> findByIdAndUserId(Long id, Long userId);
}
