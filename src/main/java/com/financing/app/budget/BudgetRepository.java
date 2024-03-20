package com.financing.app.budget;


import com.financing.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<BudgetInfo> findBudgetsByUser(User user);

    Optional<Budget> findByUserAndYearAndMonth(User user, int year, int month);
}
