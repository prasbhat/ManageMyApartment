package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ResetPassword;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_USER_OBJ;

@Controller
@SessionAttributes(MODEL_USER_OBJ)
public class LoginController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);
    private String className = this.getClass().getSimpleName();
    @Autowired
    private UserRepository userDao;

    @Autowired
    private TransactionSummaryController transactionSummaryController;

    @Autowired
    private MVController mvController;

    @PostMapping(value = "/login")
    public ModelAndView loginCheck(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers residentUser, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ResidentUsers userDb = userDao.findByEmailAddr(residentUser.getEmailAddr());
        ModelAndView mav;

        if (ManageMyApartmentUtil.isUserAuthorized(userDb, residentUser)) {
            LOGGER.info(MessageFormat.format(SUCCESS_MSG, USER, LOGIN));
            model.addAttribute(MODEL_USER_OBJ, userDb);
            model.addAttribute(MODEL_ADMIN_PAGE, Boolean.FALSE);

            mav = transactionSummaryController.getOneTransaction(userDb);
        } else {
            model.addAttribute(MODEL_LOGIN_ERROR, INVALID_LOGIN);
            mav = mvController.home();
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return mav;
    }

    @PostMapping(value = "/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute(value = MODEL_RESET_PASSWORD) ResetPassword resetPassword, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ResidentUsers userDbObj = userDao.findByEmailAddr(resetPassword.getEmailAddr());

        if (null == userDbObj) {
            model.addAttribute(MODEL_MESSAGE, INVALID_EMAIL);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        String encryptNewPass = ManageMyApartmentUtil.cryptWithMD5(resetPassword.getNewPassword());
        String encryptConfirmPass = ManageMyApartmentUtil.cryptWithMD5(resetPassword.getConfirmPassword());

        if (!encryptNewPass.equals(encryptConfirmPass)) {
            model.addAttribute(MODEL_MESSAGE, INVALID_PASSWORD_MISMATCH);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        userDbObj.setPassword(encryptNewPass);
        userDao.save(userDbObj);

        model.addAttribute(MODEL_RESET_PASS_SUCCESS, Boolean.TRUE);
        model.addAttribute(MODEL_MESSAGE, MessageFormat.format(SUCCESS_MSG, PASSWORD, UPDATED));
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_RESET_PASSWORD);
    }
}
