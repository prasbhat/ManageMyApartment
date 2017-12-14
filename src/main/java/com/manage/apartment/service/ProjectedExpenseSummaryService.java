package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.controller.ProjectedExpenseSummaryController;
import com.manage.apartment.model.ProjectedExpenseSummary;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.ProjectedExpenseSummaryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@Service
public class ProjectedExpenseSummaryService implements ManageMyApartmentConstants {

    @Autowired
    ProjectedExpenseSummaryController projectedExpenseSummaryController;

    @Autowired
    ProjectedExpenseSummaryRepository projectedExpenseSummaryRepository;

    public ModelAndView callprojectedExpenseSummaryHome(ResidentUsers userSessObj, Model model, Reports reportObj) {
        return projectedExpenseSummaryController.projectedExpenseSummaryHome(Boolean.TRUE.toString(), userSessObj,
                model, reportObj.getSelectMonth(), reportObj);
    }

    public List<ProjectedExpenseSummary> getProjectedSummaryByMonthYear(String monthYear){
        return projectedExpenseSummaryRepository.findByPrjExpSummMthYr(monthYear);
    }

    public List<ProjectedExpenseSummary> getAllProjectSummary() {
        return (List<ProjectedExpenseSummary>) projectedExpenseSummaryRepository.findAll();
    }

    public void createProjectSummary(ProjectedExpenseSummary projectedExpenseSummary) {
        projectedExpenseSummaryRepository.save(projectedExpenseSummary);
    }

    public ProjectedExpenseSummary getOneProjectSummary(int systemId) {
        return projectedExpenseSummaryRepository.findOne(systemId);
    }

    public void deleteProjectedSumamry(ProjectedExpenseSummary projectedExpenseSummary) {
        projectedExpenseSummaryRepository.delete(projectedExpenseSummary);
    }
}
