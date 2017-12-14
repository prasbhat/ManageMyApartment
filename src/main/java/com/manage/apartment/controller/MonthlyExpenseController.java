package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.MonthlyExpense;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.service.MonthlyExpenseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

/**
 * Created by 212591727 on 4/12/2017.
 */
@RestController
@SessionAttributes(MODEL_LOGIN_USER)
public class MonthlyExpenseController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(MonthlyExpenseController.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    MonthlyExpenseService monthlyExpenseService;

    @Autowired
    UserController userController;

    @Autowired
    ManageMyApartmentUtil manageMyApartmentUtil;

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/freezeMonth")
    public ModelAndView freezeMonthPage(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                           Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            model.addAttribute(MODEL_FULL_MONTH_LIST, monthlyExpenseService.findAllMonthlyExpense());
            model.addAttribute(MODEL_FREEZED_MONTH_OBJ, new MonthlyExpense());
            mav = new ModelAndView(VIEW_MONTHLY_EXPENSE);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/addFreezeMonth")
    public ModelAndView freezeCurrMonth(@ModelAttribute(value = "freezeMonthObj") MonthlyExpense monthlyExpense,
                                        @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                        BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = freezeMonthPage(userSessObj, model);

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return mav;
        }

        Map<String, String> bindErrorsMap = new HashMap<>();
        if (null == monthlyExpenseService.getMonthlyExpenseByMonthYear(monthlyExpense.getMonthYear())) {
            monthlyExpense.setFreeze(Boolean.TRUE);
            monthlyExpense.setCreationDate(new Timestamp(System.currentTimeMillis()));
            monthlyExpense.setUpdationDate(new Timestamp(System.currentTimeMillis()));
            monthlyExpenseService.createMonthlyExpense(monthlyExpense);

            recordAuditLogEntry(userSessObj.getEmailAddr(), monthlyExpense.getMonthYear().concat(FREEZE),
                    CREATE);
        } else {
            LOGGER.error(DUPLICATE_MONTH_ENTRY);
            bindErrorsMap.put(KEY_ERRORS, DUPLICATE_MONTH_ENTRY);
            model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);
        }

        model.addAttribute(MODEL_FULL_MONTH_LIST, monthlyExpenseService.findAllMonthlyExpense());

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return freezeMonthPage(userSessObj, model);
    }

    @GetMapping(value = "/editFreezeMonth/{freezeMonthId}")
    public ModelAndView editUser(@PathVariable Integer freezeMonthId,
                                 @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            MonthlyExpense monthlyExpense = monthlyExpenseService.findOneMonthlyExpense(freezeMonthId);
            if (monthlyExpense.isFreeze()) {
                monthlyExpense.setFreeze(Boolean.FALSE);
            } else {
                monthlyExpense.setFreeze(Boolean.TRUE);
            }
            monthlyExpense.setUpdationDate(new Timestamp(System.currentTimeMillis()));
            monthlyExpenseService.createMonthlyExpense(monthlyExpense);

            recordAuditLogEntry(userSessObj.getEmailAddr(), monthlyExpense.getMonthYear().concat(FREEZE +STRING_EQUAL +
                            monthlyExpense.isFreeze()), UPDATE);

            model.addAttribute(MODEL_FULL_MONTH_LIST, monthlyExpenseService.findAllMonthlyExpense());

        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return freezeMonthPage(userSessObj, model);
    }

    public void getTotalExpense(String currMonth, float totalExpense){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        MonthlyExpense monthlyExpense = monthlyExpenseService.getMonthlyExpenseByMonthYear(currMonth);

        if(null == monthlyExpense){
            monthlyExpense = new MonthlyExpense();
            monthlyExpense.setMonthYear(currMonth);
        }
        monthlyExpense.setTotalExpense(totalExpense);
        monthlyExpense.setCreationDate(new Timestamp(System.currentTimeMillis()));
        monthlyExpense.setUpdationDate(new Timestamp(System.currentTimeMillis()));
        int noOfFlats = userController.getNoOfFlats(currMonth);
        monthlyExpense.setNoOfFlats(noOfFlats);
        monthlyExpense.setOldMonthlyMaint(monthlyExpense.getMonthlyMaint());
        monthlyExpense.setMonthlyMaint((int) totalExpense / noOfFlats);
        monthlyExpense.setFreeze(Boolean.FALSE);

        float monthlyMaint = (monthlyExpense.getMonthlyMaint() - monthlyExpense.getOldMonthlyMaint());
        userController.updatePendingAmount(NUMBER_ZERO, monthlyMaint, Boolean.FALSE);

        monthlyExpenseService.createMonthlyExpense(monthlyExpense);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    public boolean isMonthOpen(String monthYear) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        MonthlyExpense monthlyExpense = monthlyExpenseService.getMonthlyExpenseByMonthYear(monthYear);
        if(null == monthlyExpense){
            return Boolean.FALSE;
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return monthlyExpense.isFreeze();
    }

    private void recordAuditLogEntry(String emailAddress, String description, String label) {
        manageMyApartmentUtil.recordAuditTrailLog(FREEZED_MONTH, emailAddress, description, FREEZE.concat(
                UNDER_SCORE+label), new Timestamp(System.currentTimeMillis()));
    }
}
