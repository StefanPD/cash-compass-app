package com.financing.app.income;

import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultIncomeService implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;

    @Transactional
    @Override
    public List<IncomeInfo> fetchIncomesByUserId(Long userId) {
        return incomeRepository.findIncomesByUser(new User(userId));
    }

    @Transactional
    @Override
    public List<IncomeDTO> fetchIncomesByHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findIncomeByUserAndIncomeDateBetween(new User(userId), startDate, endDate)
                .stream()
                .map(incomeMapper::fromIncomeToIncomeDTO)
                .toList();
    }

    @Transactional
    @Override
    public void saveIncome(Long userId, IncomeDTO incomeDTO) throws EntityNotFoundException {
        var income = incomeMapper.fromIncomeDTOtoIncome(incomeDTO);
        income.setUser(new User(userId));
        try {
            incomeRepository.save(income);
        } catch (DataIntegrityViolationException e) {
            log.error("Income failed to save - api/v1/income/{userId}/income, for userId: {}, income: {}", userId, incomeDTO);
            throw new EntityNotFoundException("User with this Id doesn't exist");
        }
    }
}
