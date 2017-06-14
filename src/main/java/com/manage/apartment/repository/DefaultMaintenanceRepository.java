package com.manage.apartment.repository;

import com.manage.apartment.model.DefaultMaintenance;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultMaintenanceRepository extends PagingAndSortingRepository<DefaultMaintenance, Integer> {
    DefaultMaintenance findByMonthYear(String monthYear);
}
