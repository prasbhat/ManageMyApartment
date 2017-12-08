package com.manage.apartment.repository;

import com.manage.apartment.model.UploadFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadFileRepository extends PagingAndSortingRepository<UploadFile, Integer> {
    List<UploadFile> findByDocUploadType(String docUploadType);
}
