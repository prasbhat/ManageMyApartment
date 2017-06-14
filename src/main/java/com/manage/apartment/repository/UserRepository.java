package com.manage.apartment.repository;

import com.manage.apartment.model.ResidentUsers;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<ResidentUsers, Integer> {
    ResidentUsers findByEmailAddr(String emailAddr);

    List<ResidentUsers> findByFlatNumber(int flatNumber);
}
