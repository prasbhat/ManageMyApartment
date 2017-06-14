package com.manage.apartment.model;

import javax.persistence.*;

/**
 * Created by 212591727 on 4/12/2017.
 */
@Entity
public class DefaultMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemId;
    private float defaultAmount;

    @Column(unique = true)
    private String monthYear;

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public float getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(float defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    @Override
    public String toString() {
        return "DefaultMaintenance{" +
                "systemId=" + systemId +
                ", defaultAmount=" + defaultAmount +
                ", monthYear=" + monthYear +
                '}';
    }
}
