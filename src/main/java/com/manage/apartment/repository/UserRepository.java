package com.manage.apartment.repository;

import com.manage.apartment.model.ResidentUsers;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<ResidentUsers, Integer> {
    ResidentUsers findByEmailAddr(String emailAddr);

    @Query("SELECT U from ResidentUsers U where (emailAddr = ?1 or flatNumber = ?2) " +
            "and additionalUserDetails.isActive = ?3")
    List<ResidentUsers> findByEmailAddrOrFlatNumberAndAdditionalUserDetails_IsActive(String emailAddr, int flatNumber,
                                                                                     boolean isActive);

    List<ResidentUsers> findByFlatNumber(int flatNumber);
}
