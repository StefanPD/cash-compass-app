package com.financing.app.savings.goals;

import com.financing.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    List<SavingGoalInfo> findSavingsGoalsByUser(User user);
}
