package com.financing.app.income;

import com.financing.app.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "incomes")
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
    private LocalDateTime incomeDate;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

