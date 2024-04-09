package com.financing.app.adapter.savings_goals.out.persistence;

import com.financing.app.application.savings_goals.port.in.SavingGoalInfo;
import com.financing.app.adapter.user.out.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    List<SavingGoalInfo> findSavingsGoalsByUser(User user);
}
