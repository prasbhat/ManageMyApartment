package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private TransactionSummaryController transactionSummaryController;

    @Autowired
    SuperAdminController auditTrailLog;

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
        return transactionSummaryController.getOneTransaction(loggedInUser);
    }

    @PostMapping(value = "/login")
    public ModelAndView loginCheck(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers loggedInUser, Model model,
                                   HttpServletRequest request) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ResidentUsers existingUser = userRepository.findByEmailAddr(loggedInUser.getEmailAddr());
        ModelAndView mav;

        if (ManageMyApartmentUtil.isUserAuthorized(existingUser, loggedInUser)) {
            LOGGER.info(MessageFormat.format(SUCCESS_MSG, USER, LOGIN));
            model.addAttribute(MODEL_LOGIN_USER, existingUser);
            request.getSession().setAttribute(MODEL_LOGIN_USER, existingUser);
//            model.addAttribute(MODEL_USER_OBJ, existingUser);
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(existingUser));
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(existingUser));

            mav = transactionSummaryController.getOneTransaction(existingUser);

            recordAuditEntry(loggedInUser.getEmailAddr(), MessageFormat.format(SUCCESS_MSG, loggedInUser.getEmailAddr(), LOGIN),
                    USER + "_" + LOGIN );

        } else {
            model.addAttribute(MODEL_LOGIN_ERROR, INVALID_LOGIN);
//            mav = mvController.home();
            mav = home();
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return mav;
    }

    @PostMapping(value = "/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute(value = MODEL_RESET_PASSWORD) ResidentUsers resetPasswordObj, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ResidentUsers userDbObj = userRepository.findByEmailAddr(resetPasswordObj.getEmailAddr());

        if (null == userDbObj) {
            model.addAttribute(MODEL_MESSAGE, EMAIL_ADDRESS_DOES_NOT_EXISTS);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        String encryptNewPass = ManageMyApartmentUtil.cryptWithMD5(resetPasswordObj.getPassword());
        String encryptConfirmPass = ManageMyApartmentUtil.cryptWithMD5(resetPasswordObj.getConfirm_password());

        if(encryptNewPass.equals(userDbObj.getPassword())){
            model.addAttribute(MODEL_MESSAGE, INVALID_NEW_PASSWORD);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        if (!encryptNewPass.equals(encryptConfirmPass)) {
            model.addAttribute(MODEL_MESSAGE, INVALID_PASSWORD_MISMATCH);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        userDbObj.setPassword(encryptNewPass);
        userRepository.save(userDbObj);

//        model.addAttribute(MODEL_RESET_PASS_SUCCESS, Boolean.TRUE);
        model.addAttribute(MODEL_MESSAGE, MessageFormat.format(SUCCESS_MSG, PASSWORD, UPDATE));

        recordAuditEntry(userDbObj.getEmailAddr(), MessageFormat.format(SUCCESS_MSG, PASSWORD, UPDATE),
                PASSWORD + "_" + UPDATE);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_RESET_PASSWORD);
    }

    private void recordAuditEntry(String emailAddress, String description, String label){
        auditTrailLog.recordAuditTrailLog(RESIDENT_USERS,emailAddress, description, label,
                new Timestamp(System.currentTimeMillis()));
    }
}
