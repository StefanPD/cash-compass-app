package com.financing.app.income;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = IncomeMapper.class,
        injectionStrategy = InjectionStrategy.FIELD)
public interface IncomeMapper {

    Income fromIncomeDTOtoIncome(IncomeDTO incomeDTO);

    IncomeDTO fromIncomeToIncomeDTO(Income income);
}
