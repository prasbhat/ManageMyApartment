package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.UserRepository;
import com.manage.apartment.service.TransactionSummaryService;
import com.manage.apartment.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.MessageFormat;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@Controller
@SessionAttributes(MODEL_LOGIN_USER)
public class LoginController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionSummaryService transactionSummaryService;

    @Autowired
    private ManageMyApartmentUtil manageMyApartmentUtil;

    @GetMapping(value = "/")
    public ModelAndView home() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_LOGIN, MODEL_USER_OBJ, new ResidentUsers());
    }

    @GetMapping(value = "/login")
    public ModelAndView logout(SessionStatus sessionStatus, HttpServletRequest request) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        sessionStatus.setComplete();
        request.getSession().removeAttribute(MODEL_LOGIN_USER);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return home();
    }

    @GetMapping(value = "/resetPassword")
    public ModelAndView resetPasswordGet() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_RESET_PASSWORD, MODEL_RESET_PASSWORD, new ResidentUsers());
    }

    @GetMapping(value = "/home")
    public ModelAndView getUserHome(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers loggedInUser, Model model){
        return transactionSummaryService.callGetOneTransaction(loggedInUser);
    }

    @PostMapping(value = "/login")
    public ModelAndView loginCheck(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers loggedInUser, Model model,
                                   HttpServletRequest request) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ResidentUsers existingUser = userService.findUsersByEmailAddress(loggedInUser.getEmailAddr());
        ModelAndView mav;

        if (ManageMyApartmentUtil.isUserAuthorized(existingUser, loggedInUser)) {
            LOGGER.info(MessageFormat.format(SUCCESS_MSG, USER, LOGIN));
            model.addAttribute(MODEL_LOGIN_USER, existingUser);
            request.getSession().setAttribute(MODEL_LOGIN_USER, existingUser);
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(existingUser));
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(existingUser));

            mav = transactionSummaryService.callGetOneTransaction(existingUser);

            recordAuditEntry(loggedInUser.getEmailAddr(), MessageFormat.format(SUCCESS_MSG, loggedInUser.getEmailAddr(),
                    LOGIN), USER.concat(UNDER_SCORE+LOGIN));

        } else {
            model.addAttribute(MODEL_LOGIN_ERROR, INVALID_LOGIN);
            mav = home();
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return mav;
    }

    private void recordAuditEntry(String emailAddress, String description, String label){
        manageMyApartmentUtil.recordAuditTrailLog(RESIDENT_USERS, emailAddress, description, label,
                new Timestamp(System.currentTimeMillis()));
    }
}
