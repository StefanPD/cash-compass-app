package com.financing.app.savings.goals;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = SavingsGoalMapper.class,
        injectionStrategy = InjectionStrategy.FIELD)
public interface SavingsGoalMapper {
    SavingsGoal fromSavingsGoalDTOtoSavingGoal(SavingsGoalDTO savingsGoalDTO);

    SavingsGoalDTO fromSavingsGoalToSavingGoalsDTO(SavingsGoal savingsGoal);
}