package com.manage.apartment.model;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class ResidentUsers {


    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
     /*String ID_PATTERN = "[0-9]+";
     String STRING_PATTERN = "[a-zA-Z]+";
 	String MOBILE_PATTERN = "[0-9]{10}";
	 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemUserId;

    @Pattern(regexp = EMAIL_PATTERN, message = "Please provide valid email address")
    @Column(unique = true)
    private String emailAddr;

    private String password;

    @Size(max = 30)
    private String firstname;
    @Size(max = 30)
    private String lastname;

    private String phoneNbr;
    private boolean isActive;
    private int flatNumber;
    private String residentStatus;
    private String userRole;
    private float pendingAmount;

    public int getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(int systemUserId) {
        this.systemUserId = systemUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
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

    public float getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(float pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    @Override
    public String toString() {
        return "ResidentUsers{" +
                "systemUserId=" + systemUserId +
                ", emailAddr='" + emailAddr + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNbr='" + phoneNbr + '\'' +
                ", isActive=" + isActive +
                ", flatNumber=" + flatNumber +
                ", residentStatus='" + residentStatus + '\'' +
                ", userRole='" + userRole + '\'' +
                ", pendingAmount=" + pendingAmount +
                '}';
    }
}
