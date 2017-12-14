package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.model.MonthlyExpense;
import com.manage.apartment.repository.MonthlyExpenseRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@Service
public class MonthlyExpenseService implements ManageMyApartmentConstants {

    @Autowired
    MonthlyExpenseRepository monthlyExpenseRepository;

    public MonthlyExpense getMonthlyExpenseByMonthYear(String monthYear){
        return monthlyExpenseRepository.findByMonthYear(monthYear);
    }

    public MonthlyExpense findOneMonthlyExpense(int freezeMonthId) {
        return monthlyExpenseRepository.findOne(freezeMonthId);
    }

    public List<MonthlyExpense> findAllMonthlyExpense(){
        return (List<MonthlyExpense>) monthlyExpenseRepository.findAll();
    }

    public void createMonthlyExpense(MonthlyExpense monthlyExpense){
        monthlyExpenseRepository.save(monthlyExpense);
    }

}
