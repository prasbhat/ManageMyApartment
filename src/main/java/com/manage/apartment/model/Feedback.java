package com.manage.apartment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemComplaintsId;

    private String complaintId;
    private String complaintText;
    private int flatNumber;
    private String username;
    private String closureComments;
    private String status;

    private Timestamp creationDate;
    private Timestamp updationDate;

    public int getSystemComplaintsId() {
        return systemComplaintsId;
    }

    public void setSystemComplaintsId(int systemComplaintsId) {
        this.systemComplaintsId = systemComplaintsId;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public int getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(int flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClosureComments() {
        return closureComments;
    }

    public void setClosureComments(String closureComments) {
        this.closureComments = closureComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Feedback{" +
                "systemComplaintsId=" + systemComplaintsId +
                ", complaintId='" + complaintId + '\'' +
                ", complaintText='" + complaintText + '\'' +
                ", flatNumber=" + flatNumber +
                ", username='" + username + '\'' +
                ", closureComments='" + closureComments + '\'' +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                '}';
    }
}
