package com.manage.apartment.repository;

import com.manage.apartment.model.AuditTrail;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditTrailRepository extends PagingAndSortingRepository<AuditTrail, Integer> {
    List<AuditTrail> findAllByOrderByLogTimeDesc();

}
