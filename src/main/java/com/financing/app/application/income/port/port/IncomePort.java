package com.financing.app.application.income.port.port;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.income.domain.model.IncomeDTO;
import com.financing.app.application.income.port.in.IncomeInfo;

import java.time.LocalDate;
import java.util.List;

public interface IncomePort {


    List<IncomeInfo> loadIncome(User user);

    List<IncomeDTO> loadIncomeHistory(User user, LocalDate startDate, LocalDate endDate);

    void saveIncome(User user, IncomeDTO incomeDTO);
}
