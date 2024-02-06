package com.financing.app.expenses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(
            value = """
                    SELECT e.expense_id, e.user_id, e.amount, e.category, e.expense_date, e.description
                    FROM expenses e
                    JOIN users u ON e.user_id = u.user_id
                    WHERE u.user_id = :userId""",
            nativeQuery = true)
    List<Expense> findExpensesByUserId(@Param("userId") Long userId);
}
