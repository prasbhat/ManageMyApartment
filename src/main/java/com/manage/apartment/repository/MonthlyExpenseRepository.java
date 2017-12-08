package com.manage.apartment.repository;

import com.manage.apartment.model.MonthlyExpense;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyExpenseRepository extends PagingAndSortingRepository<MonthlyExpense, Integer> {
    MonthlyExpense findByMonthYear(String monthYear);
}
