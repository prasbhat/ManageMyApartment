package com.manage.apartment.model;

import javax.persistence.*;
import javax.validation.Valid;

/**
 * Created by 212591727 on 8/8/2017.
 */
@Entity

public class AdditionalUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemAddlUserId;

    private boolean isActive;
    private String residentStatus;
    private String userRole;
    private String secondEmailAddr;
    private String altPhoneNbr;
    private int noOfPpl;

    @Valid
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    UploadFile uploadFile;

    public int getSystemAddlUserId() {
        return systemAddlUserId;
    }

    public void setSystemAddlUserId(int systemAddlUserId) {
        this.systemAddlUserId = systemAddlUserId;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String getResidentStatus() {
        return residentStatus;
    }

    public void setResidentStatus(String residentStatus) {
        this.residentStatus = residentStatus;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getSecondEmailAddr() {
        return secondEmailAddr;
    }

    public void setSecondEmailAddr(String secondEmailAddr) {
        this.secondEmailAddr = secondEmailAddr;
    }

    public String getAltPhoneNbr() {
        return altPhoneNbr;
    }

    public void setAltPhoneNbr(String altPhoneNbr) {
        this.altPhoneNbr = altPhoneNbr;
    }

    public int getNoOfPpl() {
        return noOfPpl;
    }

    public void setNoOfPpl(int noOfPpl) {
        this.noOfPpl = noOfPpl;
    }

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    @Override
    public String toString() {
        return "AdditionalUserDetails{" +
                "systemAddlUserId=" + systemAddlUserId +
                ", isActive=" + isActive +
                ", residentStatus='" + residentStatus + '\'' +
                ", userRole='" + userRole + '\'' +
                ", secondEmailAddr='" + secondEmailAddr + '\'' +
                ", altPhoneNbr='" + altPhoneNbr + '\'' +
                ", noOfPpl=" + noOfPpl +
                ", uploadFile=" + uploadFile +
                '}';
    }
}
