package com.financing.app.budget;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query(
            value = """
                    SELECT b.budget_id, b.user_id, b.month, b.year, b.total_budget
                    FROM budgets b
                    JOIN users u ON b.budget_id = u.user_id
                    WHERE u.user_id = :userId""",
            nativeQuery = true)
    List<Budget> findBudgetsByUserId(@Param("userId") Long userId);
}
