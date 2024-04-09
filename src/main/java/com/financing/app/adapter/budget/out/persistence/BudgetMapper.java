package com.financing.app.adapter.budget.out.persistence;


import com.financing.app.application.budget.domain.model.BudgetDTO;
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
