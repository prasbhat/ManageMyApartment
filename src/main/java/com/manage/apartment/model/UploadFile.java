package com.manage.apartment.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Arrays;

/**
 * Created by 212591727 on 8/14/2017.
 */
@Entity
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemUploadFileId;

    @Size(max = 34, message = "Filename cannot exceed 30 chars")
    private String filename;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    private Timestamp creationDate;
    private Timestamp updationDate;

    @Transient
    private MultipartFile tempFile;

    private String docUploadType;

    public int getSystemUploadFileId() {
        return systemUploadFileId;
    }

    public void setSystemUploadFileId(int systemUploadFileId) {
        this.systemUploadFileId = systemUploadFileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(Timestamp updationDate) {
        this.updationDate = updationDate;
    }

    public MultipartFile getTempFile() {
        return tempFile;
    }

    public void setTempFile(MultipartFile tempFile) {
        this.tempFile = tempFile;
    }

    public String getDocUploadType() {
        return docUploadType;
    }

    public void setDocUploadType(String docUploadType) {
        this.docUploadType = docUploadType;
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "systemUploadFileId=" + systemUploadFileId +
                ", filename='" + filename + '\'' +
                ", fileData=" + Arrays.toString(fileData) +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                ", tempFile=" + tempFile +
                ", docUploadType='" + docUploadType + '\'' +
                '}';
    }
}
