package com.financing.app.expenses;


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
