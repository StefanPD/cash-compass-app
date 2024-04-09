package com.financing.app.savings_goals.adapter.out.persistence;

import com.financing.app.savings_goals.application.port.in.SavingGoalInfo;
import com.financing.app.user.adapter.out.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    List<SavingGoalInfo> findSavingsGoalsByUser(User user);
}
