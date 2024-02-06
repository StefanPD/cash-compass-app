package com.financing.app.savings.goals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    @Query(
            value = """
                    SELECT s.saving_goal_id, s.user_id, s.saving_goal_name, s.target_amount, s.current_amount, s.start_date, s.end_date
                    FROM savings_goals s
                    JOIN users u ON s.user_id = u.user_id
                    WHERE u.user_id = :userId""",
            nativeQuery = true)
    List<SavingsGoal> findSavingsGoalsByUserId(@Param("userId") Long userId);
}
