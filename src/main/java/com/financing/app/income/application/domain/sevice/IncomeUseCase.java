package com.financing.app.income.application.domain.sevice;

import com.financing.app.user.adapter.out.User;
import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.income.application.port.in.IncomeInfo;
import com.financing.app.income.application.port.port.IncomePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class IncomeUseCase {

    private final IncomePort incomePort;

    @Transactional
    public List<IncomeInfo> fetchIncomesByUserId(Long userId) {
        return incomePort.loadIncome(new User(userId));
    }

    @Transactional
    public List<IncomeDTO> fetchIncomesByHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        return incomePort.loadIncomeHistory(new User(userId), startDate, endDate);
    }

    @Transactional
    public void saveIncome(Long userId, IncomeDTO incomeDTO) throws EntityNotFoundException {
        incomePort.saveIncome(new User(userId), incomeDTO);
    }
}
