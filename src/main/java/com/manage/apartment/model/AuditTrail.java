package com.manage.apartment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by 212591727 on 8/11/2017.
 */
@Entity
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemAuditTrailId;

    private String tableName;
    private String username;
    private String logDescription;
    private Timestamp logTime;
    private String logAction;

    public int getSystemAuditTrailId() {
        return systemAuditTrailId;
    }

    public void setSystemAuditTrailId(int systemAuditTrailId) {
        this.systemAuditTrailId = systemAuditTrailId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogDescription() {
        return logDescription;
    }

    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    public String getLogAction() {
        return logAction;
    }

    public void setLogAction(String logAction) {
        this.logAction = logAction;
    }

    @Override
    public String toString() {
        return "AuditTrail{" +
                "systemAuditTrailId=" + systemAuditTrailId +
                ", tableName='" + tableName + '\'' +
                ", username='" + username + '\'' +
                ", logDescription='" + logDescription + '\'' +
                ", logTime=" + logTime +
                ", logAction='" + logAction + '\'' +
                '}';
    }
}
