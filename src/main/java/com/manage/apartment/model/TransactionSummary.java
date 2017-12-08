package com.manage.apartment.model;

import javax.persistence.*;
import javax.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by 212591727 on 4/3/2017.
 */
@Entity
public class TransactionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemTransactionId;

    private Date date;
    private String description;
    private String expenseType; //INCOME/EXPENSE
    private float amount;
    private String modeOfPayment; //Cash/Online/Wallet
    private int flatNumber;
    private Timestamp creationDate;
    private String monthYear;

    @Valid
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    UploadFile uploadFile;

    public int getSystemTransactionId() {
        return systemTransactionId;
    }

    public void setSystemTransactionId(int systemTransactionId) {
        this.systemTransactionId = systemTransactionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    @Override
    public String toString() {
        return "TransactionSummary{" +
                "systemTransactionId=" + systemTransactionId +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", expenseType='" + expenseType + '\'' +
                ", amount=" + amount +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                ", flatNumber=" + flatNumber +
                ", creationDate=" + creationDate +
                ", monthYear='" + monthYear + '\'' +
                ", uploadFile=" + uploadFile +
                '}';
    }
}
