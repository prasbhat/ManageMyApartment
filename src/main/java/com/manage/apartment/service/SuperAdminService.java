package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.repository.UploadFileRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@Service
public class SuperAdminService implements ManageMyApartmentConstants {

    @Autowired
    UploadFileRepository uploadFileRepository;

    public List<UploadFile> getUploadFileByDocUploadType(String docUploadType) {
        return uploadFileRepository.findByDocUploadType(docUploadType);
    }

    public UploadFile getOneUploadFile(int systemUploadFileId){
        return uploadFileRepository.findOne(systemUploadFileId);
    }

    public void saveDocuments(UploadFile uploadFile) {
        uploadFileRepository.save(uploadFile);
    }

    public void deleteDocuments(int systemUploadFileId) {
        uploadFileRepository.delete(systemUploadFileId);
    }
}
