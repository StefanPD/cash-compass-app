package com.financing.app.income;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query(
            value = """
                    SELECT i.income_id, i.user_id, i.amount, i.source, i.income_date, i.description
                    FROM incomes i
                    JOIN users u ON i.user_id = u.user_id
                    WHERE u.user_id = :userId""",
            nativeQuery = true)
    List<Income> findIncomesByUserId(@Param("userId") Long userId);
}
