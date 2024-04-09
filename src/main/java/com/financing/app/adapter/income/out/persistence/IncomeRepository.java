package com.financing.app.adapter.income.out.persistence;

import com.financing.app.application.income.port.in.IncomeInfo;
import com.financing.app.adapter.user.out.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<IncomeInfo> findIncomesByUser(User user);

    List<Income> findIncomeByUserAndIncomeDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
