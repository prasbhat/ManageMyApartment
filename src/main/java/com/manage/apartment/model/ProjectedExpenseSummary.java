package com.manage.apartment.model;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 212591727 on 8/9/2017.
 */
@Entity
public class ProjectedExpenseSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemPrjExpSummId;

    private String prjExpSummDesc;
    @ColumnDefault("0.0")
    private float prjExpSummAmt;
    private String prjExpSummMthYr;
    private Timestamp creationDate;

    public int getSystemPrjExpSummId() {
        return systemPrjExpSummId;
    }

    public void setSystemPrjExpSummId(int systemPrjExpSummId) {
        this.systemPrjExpSummId = systemPrjExpSummId;
    }

    public String getPrjExpSummDesc() {
        return prjExpSummDesc;
    }

    public void setPrjExpSummDesc(String prjExpSummDesc) {
        this.prjExpSummDesc = prjExpSummDesc;
    }

    public float getPrjExpSummAmt() {
        return prjExpSummAmt;
    }

    public void setPrjExpSummAmt(float prjExpSummAmt) {
        this.prjExpSummAmt = prjExpSummAmt;
    }

    public String getPrjExpSummMthYr() {
        return prjExpSummMthYr;
    }

    public void setPrjExpSummMthYr(String prjExpSummMthYr) {
        this.prjExpSummMthYr = prjExpSummMthYr;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "ProjectedExpenseSummary{" +
                "systemPrjExpSummId=" + systemPrjExpSummId +
                ", prjExpSummDesc='" + prjExpSummDesc + '\'' +
                ", prjExpSummAmt=" + prjExpSummAmt +
                ", prjExpSummMthYr=" + prjExpSummMthYr +
                ", creationDate=" + creationDate +
                '}';
    }
}
