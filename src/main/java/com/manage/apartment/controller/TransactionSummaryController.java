package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.TransactionSummary;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.service.TransactionSummaryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

/**
 * Created by 212591727 on 4/3/2017.
 */
@RestController
@SessionAttributes(MODEL_LOGIN_USER)
public class TransactionSummaryController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(TransactionSummaryController.class);
    private String className = this.getClass().getSimpleName();
//    @Autowired
//    private TransactionSummaryRepository transactionSummaryRepository;

    @Autowired
    TransactionSummaryService transactionSummaryService;

    @Autowired
    private UserController userController;

    @Autowired
    ManageMyApartmentUtil manageMyApartmentUtil;

    String currYearMonth = new Timestamp(System.currentTimeMillis()).toString().substring(0, 7);

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    public ModelAndView getOneTransaction(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {

            mav = new ModelAndView(VIEW_HOME,
                    MODEL_TRX_SUMM_LIST, transactionSummaryService.getTransactionByFlatNumber(userSessObj.getFlatNumber()));
            mav.addObject(MODEL_USER_OBJ, userSessObj);
            mav.addObject(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            mav.addObject(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/transactionSummary")
    public ModelAndView transactionSummaryHome(@RequestParam(value = REPORTS_PARAM) String requestParam,
                                               @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                               Model model, String reportMonthYear, Reports reportObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);
            List<TransactionSummary> transactionSummaryList;

            if (requestParam.equalsIgnoreCase(Boolean.TRUE.toString())) {
                model.addAttribute(REPORTS_PAGE, Boolean.TRUE);
                model.addAttribute(MODEL_CURR_MONTH_YEAR, reportMonthYear);
                transactionSummaryList = transactionSummaryService.getTransactionByMonthYear(
                        reportObj.getSelectMonth(), reportObj.getExpenseType());
            } else {
                model.addAttribute(REPORTS_PAGE, Boolean.FALSE);
                model.addAttribute(MODEL_CURR_MONTH_YEAR, currYearMonth);
                transactionSummaryList = transactionSummaryService.getTransactionByMonthYear(currYearMonth, STRING_ALL);
            }

            model.addAttribute(MODEL_REPORT_OBJ, reportObj);
            model.addAttribute(MODEL_TRX_SUMM_LIST, transactionSummaryList);
            model.addAttribute(MODEL_TRX_SUMM_HIST_LIST, transactionSummaryService.getAllTransactionSummary());
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            model.addAttribute(MODEL_NEW_TRX_OBJ, new TransactionSummary());
            mav = new ModelAndView(VIEW_TRANSACTION_SUMMARY, MODEL_USER_OBJ, userSessObj);
        }

//        totalTransactionAmountCalculation(model);
        model.addAttribute(MODEL_TOTAL_EXP_OBJ, transactionSummaryService.getTotalTransactionAmount(currYearMonth));

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/addTransaction")
    public ModelAndView addTransactionSummary(
            @ModelAttribute(value = MODEL_NEW_TRX_OBJ) TransactionSummary transactionSummaryObj,
            @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
            BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return transactionSummaryHome(Boolean.FALSE.toString(), userSessObj, model, currYearMonth, new Reports());
        }

        if (!manageMyApartmentUtil.isMonthFreezed(transactionSummaryObj.getDate().toString().substring(0, 7))) {
            transactionSummaryObj.setCreationDate(new Timestamp(System.currentTimeMillis()));
            transactionSummaryObj.setMonthYear(transactionSummaryObj.getDate().toString().substring(0, 7));

            if(null != transactionSummaryObj.getUploadFile().getFilename()){
                UploadFile uploadFile = ManageMyApartmentUtil.uploadFileDetails(transactionSummaryObj.
                        getUploadFile(), REPORT_DOC_TYPE.transact.name());
                transactionSummaryObj.setUploadFile(uploadFile);
            }

            transactionSummaryService.saveTransactionSummary(transactionSummaryObj);
            model.addAttribute(MODEL_TOTAL_EXP_OBJ, transactionSummaryService.getTotalTransactionAmount(currYearMonth));

            if (transactionSummaryObj.getFlatNumber() != 0) {
                userController.updatePendingAmount(transactionSummaryObj.getFlatNumber(),
                        transactionSummaryObj.getAmount(),Boolean.TRUE);
            }

            recordAuditLogEntry(userSessObj.getEmailAddr(),
                    transactionSummaryObj.getDescription() + STRING_OF + transactionSummaryObj.getAmount(),
                    CREATE);
        } else {
            bindErrorsMap.put(KEY_ERRORS, MessageFormat.format(MONTH_FREEZE,
                    transactionSummaryObj.getDate().toString()));
            model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return callTransactionSummaryHome(userSessObj, model);
    }

    @GetMapping(value = "/deleteTransaction/{transactionId}")
    public ModelAndView deleteTransaction(@PathVariable int transactionId,
                                          @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                          Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        try {
            TransactionSummary transactionSummaryObj = transactionSummaryService.getOneTransactionSummary(transactionId);
            if (transactionSummaryObj.getFlatNumber() != 0) {
                userController.updatePendingAmount(transactionSummaryObj.getFlatNumber(),
                        transactionSummaryObj.getAmount(), Boolean.FALSE);
            }
            transactionSummaryService.deleteTransaction(transactionId);
//            totalTransactionAmountCalculation(model);
            model.addAttribute(MODEL_TOTAL_EXP_OBJ, transactionSummaryService.getTotalTransactionAmount(currYearMonth));

            recordAuditLogEntry(userSessObj.getEmailAddr(),
                    transactionSummaryObj.getDescription() + STRING_OF + transactionSummaryObj.getAmount(),
                    DELETE);
        } catch (Exception ex) {
            bindErrorsMap.put(KEY_ERRORS, ex.toString());
        }

        if (bindErrorsMap.size() == 0) {
            bindErrorsMap.put(KEY_SUCCESS, MessageFormat.format(SUCCESS_MSG, TRANSACTION, DELETE));
        }

        model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return callTransactionSummaryHome(userSessObj, model);
    }

//    @RequestMapping(value = "getReceiptPdf", method = RequestMethod.GET)
//    public ResponseEntity<byte[]> viewPDF(@RequestParam(value = "transactionId") int transactionId) {
////    public ResponseEntity<byte[]> viewPDF(@PathVariable(value = "transactionId") int transactionId) {
//        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
//
//        TransactionSummary transactionSummary = transactionSummaryService.getOneTransactionSummary(transactionId);
//
//        ResponseEntity<byte[]> response = ManageMyApartmentUtil.retrieveFileDetails(transactionSummary.getUploadFile());
//        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
//        return response;
//    }

    private ModelAndView callTransactionSummaryHome(ResidentUsers userSessObj, Model model){
        return transactionSummaryHome(Boolean.FALSE.toString(), userSessObj, model, currYearMonth, new Reports());
    }

    private void recordAuditLogEntry(String emailAddress, String description, String label) {
        manageMyApartmentUtil.recordAuditTrailLog(TRANSACTION_SUMMARY, emailAddress, description,
                TRANSACTION + UNDER_SCORE + label, new Timestamp(System.currentTimeMillis()));
    }
}
