package com.manage.apartment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VehicleRegistrationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int systemVehRegDetId;

//    private int flatNumber;
    private String vehicleType;
    private String registrationNumber;
    private String vehicleModel;

    public int getSystemVehRegDetId() {
        return systemVehRegDetId;
    }

    public void setSystemVehRegDetId(int systemVehRegDetId) {
        this.systemVehRegDetId = systemVehRegDetId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    @Override
    public String toString() {
        return "VehicleRegistrationDetails{" +
                "systemVehRegDetId=" + systemVehRegDetId +
                ", vehicleType='" + vehicleType + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                '}';
    }
}
