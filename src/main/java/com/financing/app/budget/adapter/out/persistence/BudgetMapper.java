package com.financing.app.budget.adapter.out.persistence;


import com.financing.app.budget.application.domain.model.BudgetDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = BudgetMapper.class,
        injectionStrategy = InjectionStrategy.FIELD)
public interface BudgetMapper {

    Budget fromBudgetDTOtoBudget(BudgetDTO budgetDTO);

    BudgetDTO fromBudgetToBudgetDTO(Budget budget);
}
