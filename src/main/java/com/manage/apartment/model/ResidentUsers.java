package com.manage.apartment.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

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

//    @Pattern(regexp = EMAIL_PATTERN, message = "Please provide valid email address")
//    @Column(unique = true)
    private String emailAddr;

    private String password;
    @Transient
    private String confirm_password;

    private Timestamp creationDate;
    private Timestamp updationDate;

    @Size(max = 30)
    private String firstname;
    @Size(max = 30)
    private String lastname;

    private String phoneNbr;

    private int flatNumber;

    private int pendingAmount;
    private String residingSince;
    private String gender;
    private String dateOfBirth;

    @Valid
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    AdditionalUserDetails additionalUserDetails;

    @Valid
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    OwnerDetails ownerDetails;

//    @Valid
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    VehicleRegistrationDetails vehicleRegistrationDetails;

    public int getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(int systemUserId) {
        this.systemUserId = systemUserId;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
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

    public String getPhoneNbr() {
        return phoneNbr;
    }

    public void setPhoneNbr(String phoneNbr) {
        this.phoneNbr = phoneNbr;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public int getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(int pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getResidingSince() {
        return residingSince;
    }

    public void setResidingSince(String residingSince) {
        this.residingSince = residingSince;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public AdditionalUserDetails getAdditionalUserDetails() {
        return additionalUserDetails;
    }

    public void setAdditionalUserDetails(AdditionalUserDetails additionalUserDetails) {
        this.additionalUserDetails = additionalUserDetails;
    }

    public OwnerDetails getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(OwnerDetails ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

   @Override
    public String toString() {
        return "ResidentUsers{" +
                "systemUserId=" + systemUserId +
                ", emailAddr='" + emailAddr + '\'' +
                ", password='" + password + '\'' +
                ", confirm_password='" + confirm_password + '\'' +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNbr='" + phoneNbr + '\'' +
                ", flatNumber=" + flatNumber +
                ", pendingAmount=" + pendingAmount +
                ", residingSince='" + residingSince + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", additionalUserDetails=" + additionalUserDetails +
                ", ownerDetails=" + ownerDetails +
                '}';
    }
}
