package com.financing.app.income;

import com.financing.app.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultIncomeService implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;

    public DefaultIncomeService(IncomeRepository incomeRepository, IncomeMapper incomeMapper) {
        this.incomeRepository = incomeRepository;
        this.incomeMapper = incomeMapper;
    }

    @Override
    public List<IncomeDTO> fetchIncomesByUserId(Long userId) {
        return incomeRepository.findIncomesByUser(new User(userId))
                .stream()
                .map(incomeMapper::fromIncomeToIncomeDTO)
                .toList();
    }
}
