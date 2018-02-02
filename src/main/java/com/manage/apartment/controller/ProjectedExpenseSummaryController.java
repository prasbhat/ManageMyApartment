package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ProjectedExpenseSummary;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.repository.ProjectedExpenseSummaryRepository;
import com.manage.apartment.service.ProjectedExpenseSummaryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

/**
 * Created by 212591727 on 8/9/2017.
 */
@RestController
@SessionAttributes(MODEL_LOGIN_USER)
public class ProjectedExpenseSummaryController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(ProjectedExpenseSummaryController.class);
    private String className = this.getClass().getSimpleName();

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @Autowired
    ProjectedExpenseSummaryService prjExpSummService;

    @Autowired
    MonthlyExpenseController monthlyExpenseController;

    @Autowired
    ManageMyApartmentUtil manageMyApartmentUtil;

    String currYearMonth = new Timestamp(System.currentTimeMillis()).toString().substring(0, 7);

    @GetMapping(value = "/projectedExpenseSummary")
    public ModelAndView projectedExpenseSummaryHome(@RequestParam(value = REPORTS_PARAM) String requestParam,
                                                    @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                                    Model model, String reportMonthYear, Reports reportObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);
            List<ProjectedExpenseSummary> projectedExpenseSummaryList;

            if (requestParam.equalsIgnoreCase(Boolean.TRUE.toString())) {
                model.addAttribute(REPORTS_PAGE, Boolean.TRUE);
                model.addAttribute(MODEL_CURR_MONTH_YEAR, reportMonthYear);
                projectedExpenseSummaryList = prjExpSummService.getProjectedSummaryByMonthYear(reportMonthYear);
            } else {
                model.addAttribute(REPORTS_PAGE, Boolean.FALSE);
                model.addAttribute(MODEL_CURR_MONTH_YEAR, currYearMonth);
                projectedExpenseSummaryList = prjExpSummService.getProjectedSummaryByMonthYear(currYearMonth);
            }
            model.addAttribute(MODEL_REPORT_OBJ, reportObj);
            model.addAttribute(MODEL_PRJ_EXP_SUMM_OBJ_LIST, projectedExpenseSummaryList);

            model.addAttribute(MODEL_PRJ_EXP_SUMM_OBJ, new ProjectedExpenseSummary());
            model.addAttribute(MODEL_IS_PROJECTED_ALLLOWED, (new Timestamp(System.currentTimeMillis())
                    .toLocalDateTime().getDayOfMonth() <= 5));
            model.addAttribute(MODEL_PRJ_EXP_SUMM_HIST_OBJ_LIST, prjExpSummService.getAllProjectSummary());

            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            mav = new ModelAndView(VIEW_PROJECTED_EXPENSE_SUMMARY);
        }

        totalAmountCalculation(model);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/prjExpSummAdd")
    public ModelAndView projectedExpenseSummaryAdd(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                                   @ModelAttribute(value = MODEL_PRJ_EXP_SUMM_OBJ) ProjectedExpenseSummary
                                                           projectedExpenseSummary, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        if (!manageMyApartmentUtil.isMonthFreezed(projectedExpenseSummary.getPrjExpSummMthYr())) {
            projectedExpenseSummary.setCreationDate(new Timestamp(System.currentTimeMillis()));

            if(0 < projectedExpenseSummary.getUploadFile().getTempFile().getSize()){
                UploadFile uploadFile = ManageMyApartmentUtil.uploadFileDetails(projectedExpenseSummary.
                        getUploadFile(), REPORT_DOC_TYPE.project.name());
                projectedExpenseSummary.setUploadFile(uploadFile);
            }

            prjExpSummService.createProjectSummary(projectedExpenseSummary);
            totalAmountCalculation(model);

            recordAuditLogEntry(userSessObj.getEmailAddr(),
                    MessageFormat.format(SUCCESS_MSG, projectedExpenseSummary.getPrjExpSummDesc(), CREATE), CREATE);
        } else {
            bindErrorsMap.put(KEY_ERRORS, MessageFormat.format(MONTH_FREEZE, projectedExpenseSummary.getPrjExpSummMthYr()));
            model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return projectedExpenseSummaryHome(Boolean.FALSE.toString(), userSessObj, model, currYearMonth, new Reports());
    }

    @GetMapping(value = "/prjExpSummDel/{prjExpSummId}")
    public ModelAndView deleteTransaction(@PathVariable int prjExpSummId,
                                          @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                          Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        try {
            ProjectedExpenseSummary projectedExpenseSummary = prjExpSummService.getOneProjectSummary(prjExpSummId);

            prjExpSummService.deleteProjectedSumamry(projectedExpenseSummary);
            totalAmountCalculation(model);

            recordAuditLogEntry(userSessObj.getEmailAddr(),
                    MessageFormat.format(SUCCESS_MSG, projectedExpenseSummary.getPrjExpSummDesc(), DELETE), DELETE);
        } catch (Exception ex) {
            bindErrorsMap.put(KEY_ERRORS, ex.toString());
        }

        if (bindErrorsMap.size() == 0) {
            bindErrorsMap.put(KEY_SUCCESS, MessageFormat.format(SUCCESS_MSG, TRANSACTION, DELETE));
        }

        model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return projectedExpenseSummaryHome(Boolean.FALSE.toString(), userSessObj, model, currYearMonth, new Reports());
    }

    @GetMapping(value = "/calculateExpense")
    public ModelAndView calculateTotalExpense(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                              Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        float sumOfExpense = 0;

        List<ProjectedExpenseSummary> projectedExpenseSummaryList = prjExpSummService.getProjectedSummaryByMonthYear(
                currYearMonth);

        for (ProjectedExpenseSummary prjExpSummModel : projectedExpenseSummaryList) {
            sumOfExpense += prjExpSummModel.getPrjExpSummAmt();
        }

        monthlyExpenseController.getTotalExpense(currYearMonth, sumOfExpense);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return monthlyExpenseController.freezeMonthPage(userSessObj, model);
    }

    private void totalAmountCalculation(Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        float sumOfExpense = 0;

        List<ProjectedExpenseSummary> projectedExpenseSummaryList = prjExpSummService.getProjectedSummaryByMonthYear(
                currYearMonth);

        for (ProjectedExpenseSummary prjExpSummModel : projectedExpenseSummaryList) {
            sumOfExpense += prjExpSummModel.getPrjExpSummAmt();
        }

        model.addAttribute(MODEL_TOTAL_EXP_OBJ, sumOfExpense);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    private void recordAuditLogEntry(String emailAddress, String description, String label) {
        manageMyApartmentUtil.recordAuditTrailLog(PROJECTED_EXPENSE_SUMMARY, emailAddress, description,
                PROJECTED_EXPENSE_SUMMARY.concat(UNDER_SCORE + label), new Timestamp(System.currentTimeMillis()));
    }
}
