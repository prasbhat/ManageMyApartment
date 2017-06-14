package com.manage.apartment.model;

/**
 * Created by 212591727 on 3/27/2017.
 */
public class ResetPassword {
    private String emailAddr;
    private String newPassword;
    private String confirmPassword;

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "ResetPassword{" +
                "emailAddr='" + emailAddr + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
