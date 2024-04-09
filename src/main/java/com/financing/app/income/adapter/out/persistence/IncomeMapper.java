package com.financing.app.income.adapter.out.persistence;

import com.financing.app.income.application.domain.model.IncomeDTO;
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
