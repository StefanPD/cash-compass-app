package com.financing.app.income.adapter.out.persistence;

import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.income.application.port.in.IncomeInfo;
import com.financing.app.income.application.port.port.IncomePort;
import com.financing.app.user.adapter.out.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    public void saveIncome(User user, IncomeDTO incomeDTO) {
        var income = incomeMapper.fromIncomeDTOtoIncome(incomeDTO);
        income.setUser(user);
        incomeRepository.save(income);
    }

    @Override
    public void updateIncome(User user, IncomeDTO incomeDTO) {
        incomeRepository
                .findById(incomeDTO.getIncomeId())
                .map(item -> {
                    if (!Objects.equals(item.getUser().getUserId(), user.getUserId())) {
                        throw new EntityNotFoundException("Entity not found");
                    }
                    item.setIncomeDate(incomeDTO.getIncomeDate());
                    item.setAmount(incomeDTO.getAmount());
                    item.setSource(incomeDTO.getSource());
                    item.setDescription(incomeDTO.getDescription());
                    return incomeRepository.save(item);
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteIncome(User user, Long budgetId) throws EntityNotFoundException {
        incomeRepository
                .findById(budgetId)
                .map(item -> {
                    if (!Objects.equals(user.getUserId(), item.getUser().getUserId())) {
                        throw new EntityNotFoundException("Entity not found");
                    }
                    incomeRepository.delete(item);
                    return item;
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}
