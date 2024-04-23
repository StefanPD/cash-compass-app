package com.financing.app.income.application.port.port;

import com.financing.app.user.adapter.out.User;
import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.income.application.port.in.IncomeInfo;

import java.time.LocalDate;
import java.util.List;

public interface IncomePort {


    List<IncomeInfo> loadIncome(User user);

    List<IncomeDTO> loadIncomeHistory(User user, LocalDate startDate, LocalDate endDate);

    void saveIncome(User user, IncomeDTO incomeDTO);

    void updateIncome(User user, IncomeDTO incomeDTO);

    void deleteIncome(User user, Long incomeId);
}
