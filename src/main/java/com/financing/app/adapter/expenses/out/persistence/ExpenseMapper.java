package com.financing.app.adapter.expenses.out.persistence;


import com.financing.app.application.expenses.domain.model.ExpenseDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = ExpenseMapper.class,
        injectionStrategy = InjectionStrategy.FIELD)
public interface ExpenseMapper {

    Expense fromExpenseDTOtoExpense(ExpenseDTO expenseDTO);

    ExpenseDTO fromExpenseToExpenseDTO(Expense expense);
}
