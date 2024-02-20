package com.financing.app.income;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class IncomeDateTransformer {
    private final Map<Integer, String> monthNames = Map.ofEntries(
            Map.entry(1, "January"),
            Map.entry(2, "February"),
            Map.entry(3, "March"),
            Map.entry(4, "April"),
            Map.entry(5, "May"),
            Map.entry(6, "June"),
            Map.entry(7, "July"),
            Map.entry(8, "August"),
            Map.entry(9, "September"),
            Map.entry(10, "October"),
            Map.entry(11, "November"),
            Map.entry(12, "December")
    );

    public Map<Integer, Map<String, List<IncomeDTO>>> transform(List<IncomeDTO> incomes) {
        Map<Integer, Map<String, List<IncomeDTO>>> response = new TreeMap<>(); // Outer map sorted by year
        for (IncomeDTO income : incomes) {
            int year = income.getIncomeDate().getYear();
            int month = income.getIncomeDate().getMonthValue();
            String monthName = monthNames.get(month);
            response.computeIfAbsent(year, k -> {
                Map<String, List<IncomeDTO>> monthMap = new LinkedHashMap<>();
                monthNames.forEach((num, name) -> monthMap.put(name, new ArrayList<>()));
                return monthMap;
            }).get(monthName).add(income);
        }

        return response;
    }

}
