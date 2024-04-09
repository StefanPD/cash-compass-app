package com.financing.app.adapter.income.out.persistence;

import com.financing.app.adapter.user.out.User;
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
@Table(name = "incomes", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "income_id")
    private Long incomeId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private String source;

    @Column(name = "income_date")
    private LocalDate incomeDate;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Income(BigDecimal amount, String source, LocalDate incomeDate, String description, User user) {
        this.amount = amount;
        this.source = source;
        this.incomeDate = incomeDate;
        this.description = description;
        this.user = user;
    }
}

