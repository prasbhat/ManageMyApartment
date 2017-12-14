package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@RestController
@SessionAttributes(MODEL_LOGIN_USER)
public class ReportsController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(ReportsController.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    private ManageMyApartmentUtil manageMyApartmentUtil;

    @Autowired
    private TransactionSummaryService transactionSummaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectedExpenseSummaryService projectedExpenseSummaryService;

    @Autowired
    MonthlyExpenseService monthlyExpenseService;

    @Autowired
    SuperAdminService superAdminService;

    String currYearMonth = new Timestamp(System.currentTimeMillis()).toString().substring(0, 7);

    Map<String, Object> reportConfigMap = new HashMap();

    @Value("${mainTitleText}")
    String MAIN_TITLE;

    @Value("${regDtlText}")
    String REG_DTL_TEXT;

    @Value("${addrText}")
    String ADDR_DTL_TEXT;

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createNewUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/monthlyReport")
    public ModelAndView displayMonthlyReport(
            @RequestParam(value = REPORTS_PARAM) String requestParam,
            @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        ManageMyApartmentUtil.modelDataLoad(model);
        if (null == mav) {
            model = addAttributesToModel(model, userSessObj);
            Reports reports = new Reports();
            reports.setSelectMonth(currYearMonth);
            reports.setReportsType(requestParam);

            model.addAttribute(MODEL_REPORT_OBJ, reports);
            model.addAttribute(MODEL_IS_REPORT_TRANSACT, requestParam.equals(REPORT_DOC_TYPE.transact.name()));
            mav = new ModelAndView(FOLDER_REPORTS + VIEW_MONTHLY_REPORT);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/getMonthlyReport")
    public ModelAndView getMonthlyReport(
            @ModelAttribute(value = MODEL_REPORT_OBJ) Reports reportObj,
            @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model = addAttributesToModel(model, userSessObj);
            getUtilInfo(reportObj, model, userSessObj);

            mav = (ModelAndView) reportConfigMap.get(MAV);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    /**
     * Handle request to download an PDF document
     */
    @PostMapping(value = "/downloadReport")
    public ModelAndView downloadReports(
            @ModelAttribute Reports reportObj,
            @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
            Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model = addAttributesToModel(model, userSessObj);

            getUtilInfo(reportObj, model, userSessObj);

            mav = (ModelAndView) reportConfigMap.get(MAV);

            manageMyApartmentUtil.generateReports(response, reportConfigMap);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "getPdf")
    public ResponseEntity<byte[]> viewPDF(@RequestParam(value = "systemId") int systemId,
                                          @RequestParam(value = "docType") String docUploadType) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        UploadFile fileForView = new UploadFile();
        switch (REPORT_DOC_TYPE.valueOf(docUploadType)) {
            case super_admin:
                fileForView = superAdminService.getOneUploadFile(systemId);
                break;
            case transact:
                fileForView = transactionSummaryService.getOneTransactionSummary(systemId).getUploadFile();
                break;
            case user:
                fileForView = userService.findOneUser(systemId).getAdditionalUserDetails().getUploadFile();
                break;
            default:
                break;
        }
        ResponseEntity<byte[]> response = ManageMyApartmentUtil.retrieveFileDetails(fileForView);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return response;
    }

    private void getUtilInfo(Reports reportObj, Model model, ResidentUsers userSessObj) {

        switch (REPORT_DOC_TYPE.valueOf(reportObj.getReportsType())) {
            case transact:
                reportConfigMap.put(MAV, transactionSummaryService.callTransactionSummaryHome(userSessObj, model,
                        reportObj));
                reportConfigMap.put(OBJ_DATA_LIST, transactionSummaryService.getTransactionByMonthYear(
                        reportObj.getSelectMonth(), reportObj.getExpenseType()));
                reportConfigMap.put(REPORT_NAME, FOLDER_REPORTS + VIEW_TRX_SUMM_REPORT);
                reportConfigMap.put(PDF_FILENAME, PDF_TRX_REPORTS);
                reportConfigMap.put(TITLE_TEXT, reportObj.getSelectMonth());
                reportConfigMap.put(MODEL_TOTAL_EXP_OBJ, transactionSummaryService.getTotalTransactionAmount(
                        reportObj.getSelectMonth()));
                break;
            case user:
                reportConfigMap.put(MAV, userService.callGetAllUsers(userSessObj));
                reportConfigMap.put(OBJ_DATA_LIST, userService.getAllUserList());
                reportConfigMap.put(REPORT_NAME, FOLDER_REPORTS + VIEW_USER_REPORTS);
                reportConfigMap.put(PDF_FILENAME, PDF_USER_REPORTS);
                break;
            case project:
                reportConfigMap.put(MAV, projectedExpenseSummaryService.callprojectedExpenseSummaryHome(userSessObj, model,
                        reportObj));
                reportConfigMap.put(OBJ_DATA_LIST, projectedExpenseSummaryService.
                        getProjectedSummaryByMonthYear(reportObj.getSelectMonth()));
                reportConfigMap.put(REPORT_NAME, FOLDER_REPORTS + VIEW_PROJECT_SUMMARY_REPORTS);
                reportConfigMap.put(PDF_FILENAME, PDF_PROJECTED_SUMMARY_REPORTS);
                reportConfigMap.put(TITLE_TEXT, reportObj.getSelectMonth());
                reportConfigMap.put(MODEL_FULL_MONTH_LIST, monthlyExpenseService.getMonthlyExpenseByMonthYear(
                        reportObj.getSelectMonth()));
                break;
            default:
                break;
        }
    }

    private Model addAttributesToModel(Model model, ResidentUsers userSessObj) {
        ManageMyApartmentUtil.modelDataLoad(model);
        model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
        model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));

        reportConfigMap.put(APT_NAME, MAIN_TITLE);
        reportConfigMap.put(REG_DTL_TXT, REG_DTL_TEXT);
        reportConfigMap.put(ADDR_TEXT, ADDR_DTL_TEXT);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN_REPORT);
        reportConfigMap.put(PRINT_DATE, dtf.format(LocalDateTime.now()));
        reportConfigMap.put(PRINT_NAME, userSessObj.getEmailAddr());
        return model;
    }

}
