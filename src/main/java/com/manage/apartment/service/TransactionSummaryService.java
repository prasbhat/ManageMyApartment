package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.controller.TransactionSummaryController;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.TransactionSummary;
import com.manage.apartment.repository.TransactionSummaryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class TransactionSummaryService implements ManageMyApartmentConstants {

    @Autowired
    private TransactionSummaryRepository transactionSummaryRepository;

    @Autowired
    TransactionSummaryController transactionSummaryController;

    public List<TransactionSummary> getTransactionByFlatNumber(int flatNumber) {
        return transactionSummaryRepository.findByFlatNumber(flatNumber);
    }

    public TransactionSummary getOneTransactionSummary(int transactionId){
        return transactionSummaryRepository.findOne(transactionId);
    }

    public List<TransactionSummary> getTransactionByMonthYear(String monthYear, String expenseType){
        if(!expenseType.equals(STRING_ALL))
            return transactionSummaryRepository.findByMonthYearAndExpenseType(monthYear, expenseType);
        return transactionSummaryRepository.findByMonthYear(monthYear);
    }

    public List<TransactionSummary> getAllTransactionSummary(){
        return (List<TransactionSummary>) transactionSummaryRepository.findAll();
    }

    public void saveTransactionSummary(TransactionSummary transactionSummary) {
        transactionSummaryRepository.save(transactionSummary);
    }

    public void deleteTransaction(int transactionId){
        transactionSummaryRepository.delete(transactionId);
    }

    public float getTotalTransactionAmount(String currYearMonth) {
        float sumOfExpense = 0;

        List<TransactionSummary> transactionSummaryList = getTransactionByMonthYear(currYearMonth, STRING_ALL);

        for (TransactionSummary transactionSummary : transactionSummaryList) {
            if (transactionSummary.getExpenseType().equals(EXPENSE_TYPE.EXPENSE.toString())) {
                sumOfExpense -= transactionSummary.getAmount();
            } else {
                sumOfExpense += transactionSummary.getAmount();
            }
        }
        return sumOfExpense;
    }

    public ModelAndView callTransactionSummaryHome(ResidentUsers residentUsers, Model model, Reports reportObj) {
       return transactionSummaryController.transactionSummaryHome(Boolean.TRUE.toString(), residentUsers,
                model, reportObj.getSelectMonth(), reportObj);
    }

    public ModelAndView callGetOneTransaction(ResidentUsers residentUsers) {
        return transactionSummaryController.getOneTransaction(residentUsers);
    }
}
