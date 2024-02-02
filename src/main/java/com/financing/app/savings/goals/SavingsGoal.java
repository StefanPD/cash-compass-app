package com.financing.app.savings.goals;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "savings_goals")
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saving_goal_id")
    private Long savingGoalId;

    @Column(name= "saving_goal_name")
    private String name;

    @Column(precision = 10, scale = 2, name = "target_amount")
    private BigDecimal targetAmount;

    @Column(precision = 10, scale = 2, name = "current_amount")
    private BigDecimal currentAmount;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
