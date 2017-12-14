package com.manage.apartment.model;

public class Reports {

    private String selectMonth;
    private String reportsType;
    private String expenseType;

    public String getSelectMonth() {
        return selectMonth;
    }

    public String getReportsType() {
        return reportsType;
    }

    public void setReportsType(String reportsType) {
        this.reportsType = reportsType;
    }

    public void setSelectMonth(String selectMonth) {
        this.selectMonth = selectMonth;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    @Override
    public String toString() {
        return "Reports{" +
                "selectMonth='" + selectMonth + '\'' +
                ", reportsType='" + reportsType + '\'' +
                ", expenseType='" + expenseType + '\'' +
                '}';
    }
}
