package com.financing.app.adapter.savings_goals.out.persistence;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "savings_goals", schema = "public")
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_goal_id")
    private Long savingGoalId;

    @Column(name = "saving_goal_name")
    private String name;

    @Column(precision = 10, scale = 2, name = "target_amount")
    private BigDecimal targetAmount;

    @Column(precision = 10, scale = 2, name = "current_amount")
    private BigDecimal currentAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
