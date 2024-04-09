package com.financing.app.adapter.income.out.persistence;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.income.domain.model.IncomeDTO;
import com.financing.app.application.income.port.in.IncomeInfo;
import com.financing.app.application.income.port.port.IncomePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomePersistenceAdapter implements IncomePort {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;

    @Override
    public List<IncomeInfo> loadIncome(User user) {
        return incomeRepository.findIncomesByUser(user);
    }

    @Override
    public List<IncomeDTO> loadIncomeHistory(User user, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findIncomeByUserAndIncomeDateBetween(user, startDate, endDate)
                .stream()
                .map(incomeMapper::fromIncomeToIncomeDTO)
                .toList();
    }

    @Override
    public void saveIncome(User user, IncomeDTO incomeDTO) throws EntityNotFoundException {
        var income = incomeMapper.fromIncomeDTOtoIncome(incomeDTO);
        income.setUser(user);
        try {
            incomeRepository.save(income);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("User with this Id doesn't exist");
        }
    }
}
