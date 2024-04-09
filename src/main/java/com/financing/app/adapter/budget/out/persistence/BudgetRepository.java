package com.financing.app.adapter.budget.out.persistence;


import com.financing.app.application.budget.port.in.BudgetInfo;
import com.financing.app.adapter.user.out.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<BudgetInfo> findBudgetsByUser(User user);

    Optional<Budget> findByUserAndYearAndMonth(User user, int year, int month);
}
