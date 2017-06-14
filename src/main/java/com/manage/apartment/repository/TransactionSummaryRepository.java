package com.manage.apartment.repository;

import com.manage.apartment.model.TransactionSummary;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionSummaryRepository extends PagingAndSortingRepository<TransactionSummary, Integer> {
    List<TransactionSummary> findByFlatNumber(int flatNumber);
}
