package com.financing.app.budget.adapter.out.persistence;


import com.financing.app.budget.application.port.in.BudgetInfo;
import com.financing.app.user.adapter.out.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<BudgetInfo> findBudgetsByUser(User user);

    Optional<Budget> findByUserAndYearAndMonth(User user, int year, int month);
}
