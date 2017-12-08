package com.manage.apartment.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 212591727 on 8/16/2017.
 */
@Entity
public class MonthlyExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemMonthlyExpenseId;

    @Column(unique = true)
    private String monthYear;
    private Timestamp creationDate;
    private Timestamp updationDate;

    private float totalExpense;
    private int noOfFlats;
    private int monthlyMaint;
    private int oldMonthlyMaint;
    private boolean freeze;

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public int getSystemMonthlyExpenseId() {
        return systemMonthlyExpenseId;
    }

    public void setSystemMonthlyExpenseId(int systemMonthlyExpenseId) {
        this.systemMonthlyExpenseId = systemMonthlyExpenseId;
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

    public float getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(float totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int getNoOfFlats() {
        return noOfFlats;
    }

    public void setNoOfFlats(int noOfFlats) {
        this.noOfFlats = noOfFlats;
    }

    public int getMonthlyMaint() {
        return monthlyMaint;
    }

    public void setMonthlyMaint(int monthlyMaint) {
        this.monthlyMaint = monthlyMaint;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public int getOldMonthlyMaint() {
        return oldMonthlyMaint;
    }

    public void setOldMonthlyMaint(int oldMonthlyMaint) {
        this.oldMonthlyMaint = oldMonthlyMaint;
    }

    @Override
    public String toString() {
        return "MonthlyExpense{" +
                "systemMonthlyExpenseId=" + systemMonthlyExpenseId +
                ", monthYear='" + monthYear + '\'' +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                ", totalExpense=" + totalExpense +
                ", noOfFlats=" + noOfFlats +
                ", monthlyMaint=" + monthlyMaint +
                ", oldMonthlyMaint=" + oldMonthlyMaint +
                ", freeze=" + freeze +
                '}';
    }
}
