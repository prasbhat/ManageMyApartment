package com.manage.apartment.repository;

import com.manage.apartment.model.ProjectedExpenseSummary;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectedExpenseSummaryRepository extends PagingAndSortingRepository<ProjectedExpenseSummary, Integer> {
    List<ProjectedExpenseSummary> findByPrjExpSummMthYr(String prjExpSummMthYr);
}
