package com.financing.app.expenses.adapter.out.persistence;

import com.financing.app.expenses.application.port.in.ExpenseInfo;
import com.financing.app.user.adapter.out.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<ExpenseInfo> findExpensesByUser(User user);

    @Query(value = "SELECT * FROM expenses WHERE user_id = :userId AND EXTRACT(YEAR FROM expense_date) = :year AND EXTRACT(MONTH FROM expense_date) = :month", nativeQuery = true)
    Page<Expense> findByUserIdAndYearAndMonth(Long userId, int year, int month, Pageable pageable);

    @Query(value = "SELECT sum(amount) FROM expenses WHERE user_id = :userId AND EXTRACT(YEAR FROM expense_date) = :year AND EXTRACT(MONTH FROM expense_date) = :month", nativeQuery = true)
    BigDecimal calculateExpenseForSpecificMonth(Long userId, int year, int month);

    List<ExpenseInfo> findExpensesByUserAndExpenseDate(User user, LocalDate expenseDate);
}
