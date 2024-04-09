package com.financing.app.application.expenses.port.in;

import com.financing.app.application.expenses.domain.model.ExpenseDTO;

import java.util.List;

public record ExpensePage(
        List<ExpenseDTO> expenses,
        int currentPage,
        int totalPages,
        Long totalElements
) {
}
