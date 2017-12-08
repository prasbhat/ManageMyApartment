package com.manage.apartment.repository;

import com.manage.apartment.model.Feedback;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends PagingAndSortingRepository<Feedback, Integer> {
    List<Feedback> findByFlatNumber(int flatNumber);
}
