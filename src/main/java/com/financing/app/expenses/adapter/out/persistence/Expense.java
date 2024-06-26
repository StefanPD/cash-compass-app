package com.financing.app.expenses.adapter.out.persistence;

import com.financing.app.user.adapter.out.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "expenses", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expensesId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private String category;

    @Column(name = "expense_date")
    private LocalDate expenseDate;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

