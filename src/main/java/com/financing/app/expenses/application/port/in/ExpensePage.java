package com.financing.app.expenses.application.port.in;

import com.financing.app.expenses.application.domain.model.ExpenseDTO;

import java.util.List;

public record ExpensePage(
        List<ExpenseDTO> expenses,
        int currentPage,
        int totalPages,
        Long totalElements
) {
}
