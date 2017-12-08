package com.manage.apartment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Created by 212591727 on 8/8/2017.
 */
@Entity
public class OwnerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemOwnerId;

    @Size(max = 30)
    private String ownerFirstname;
    @Size(max = 30)
    private String ownerLastname;
    private String ownerEmailAddr;

    private String ownerPhoneNbr;
    private String ownerAddr;

    public int getSystemOwnerId() {
        return systemOwnerId;
    }

    public void setSystemOwnerId(int systemOwnerId) {
        this.systemOwnerId = systemOwnerId;
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public void setOwnerFirstname(String ownerFirstname) {
        this.ownerFirstname = ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public String getOwnerEmailAddr() {
        return ownerEmailAddr;
    }

    public void setOwnerEmailAddr(String ownerEmailAddr) {
        this.ownerEmailAddr = ownerEmailAddr;
    }

    public String getOwnerPhoneNbr() {
        return ownerPhoneNbr;
    }

    public void setOwnerPhoneNbr(String ownerPhoneNbr) {
        this.ownerPhoneNbr = ownerPhoneNbr;
    }

    public String getOwnerAddr() {
        return ownerAddr;
    }

    public void setOwnerAddr(String ownerAddr) {
        this.ownerAddr = ownerAddr;
    }

    @Override
    public String toString() {
        return "OwnerDetails{" +
                "systemOwnerId=" + systemOwnerId +
                ", ownerFirstname='" + ownerFirstname + '\'' +
                ", ownerLastname='" + ownerLastname + '\'' +
                ", ownerEmailAddr='" + ownerEmailAddr + '\'' +
                ", ownerPhoneNbr='" + ownerPhoneNbr + '\'' +
                ", ownerAddr='" + ownerAddr + '\'' +
                '}';
    }
}
