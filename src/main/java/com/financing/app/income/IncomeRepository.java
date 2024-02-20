package com.financing.app.income;

import com.financing.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findIncomesByUser(User user);

    List<Income> findIncomeByUserAndIncomeDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
