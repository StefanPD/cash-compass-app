package com.financing.app.expenses;

import java.util.List;

public record ExpensePage(
        List<ExpenseDTO> expenses,
        int currentPage,
        int totalPages,
        Long totalElements
) {
}
