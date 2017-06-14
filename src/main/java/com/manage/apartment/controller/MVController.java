package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ResetPassword;
import com.manage.apartment.model.ResidentUsers;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_USER_OBJ;

/**
 * Created by 212591727 on 4/3/2017.
 */
@RestController
@SessionAttributes(MODEL_USER_OBJ)
public class MVController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(MVController.class);
    private String className = this.getClass().getSimpleName();

    @ModelAttribute(value = MODEL_USER_OBJ)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/")
    public ModelAndView home() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_LOGIN, MODEL_USER_OBJ, new ResidentUsers());
    }

    @GetMapping(value = "/register")
    public ModelAndView registerUser(Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ManageMyApartmentUtil.modelDataLoad(model);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_REGISTER_USER, MODEL_USER_OBJ, new ResidentUsers());
    }

    @GetMapping(value = "/resetPassword")
    public ModelAndView resetPasswordGet(Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_RESET_PASSWORD, MODEL_RESET_PASSWORD, new ResetPassword());
    }

    @GetMapping(value = "/login")
    public ModelAndView logout() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return home();
    }

    @GetMapping(value = "/createUser")
    public ModelAndView invalidCreateUser() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return home();
    }

    @GetMapping(value = "/updateUser")
    public ModelAndView invalidUpdateUser() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return home();
    }

    @GetMapping(value = "/addTransaction")
    public ModelAndView invalidAddTransaction() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return home();
    }

    @GetMapping(value = "/defaultAmount")
    public ModelAndView invalidDefaultAmount() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return home();
    }
}
