package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.model.TransactionSummary;
import com.manage.apartment.repository.TransactionSummaryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionSummaryService implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(TransactionSummaryService.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    private TransactionSummaryRepository transactionSummaryRepository;

    public List<TransactionSummary> getTransactionByFlatNumber(int flatNumber) {
        return transactionSummaryRepository.findByFlatNumber(flatNumber);
    }

    public TransactionSummary getOneTransactionSummary(int transactionId){
        return transactionSummaryRepository.findOne(transactionId);
    }

    public List<TransactionSummary> getTransactionByMonthYear(String monthYear){
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

        List<TransactionSummary> transactionSummaryList = getTransactionByMonthYear(currYearMonth);

        for (TransactionSummary transactionSummary : transactionSummaryList) {
            if (transactionSummary.getExpenseType().equals(EXPENSE_TYPE.EXPENSE.toString())) {
                sumOfExpense -= transactionSummary.getAmount();
            } else {
                sumOfExpense += transactionSummary.getAmount();
            }
        }
        return sumOfExpense;
    }


}
