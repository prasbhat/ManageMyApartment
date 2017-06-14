package com.manage.apartment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

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

    @Override
    public String toString() {
        return "TransactionSummary{" +
                "systemTransactionId=" + systemTransactionId +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", expenseType='" + expenseType + '\'' +
                ", amount=" + amount +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                ", flatNumber='" + flatNumber + '\'' +
                '}';
    }
}
