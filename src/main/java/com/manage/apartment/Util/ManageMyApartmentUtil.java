package com.manage.apartment.Util;

import com.manage.apartment.controller.MVController;
import com.manage.apartment.model.ResidentUsers;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 212591727 on 3/24/2017.
 */
public class ManageMyApartmentUtil implements ManageMyApartmentConstants {
    private static final Logger LOGGER = Logger.getLogger(ManageMyApartmentUtil.class);
    private static MessageDigest md;
    private static String className = "ManageMyApartmentUtil";

    public static boolean isUserAuthorized(ResidentUsers dbUser, ResidentUsers loginUser) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        String encryptPass = ManageMyApartmentUtil.cryptWithMD5(loginUser.getPassword());

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return (dbUser != null && dbUser.getEmailAddr().equals(loginUser.getEmailAddr()) &&
                dbUser.getPassword().equals(encryptPass) && dbUser.getIsActive());
    }

    public static String cryptWithMD5(String pass) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error(ex);
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return null;
    }

    public static ModelAndView printHibernateValidatorConstraints(BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
//        ArrayList<JSONObject> errorJsonArr = new ArrayList<>();
//
//        JSONObject violationMap = new JSONObject();
//        for (ConstraintViolation violations : constraintViolations) {
//            violationMap.put(violations.getPropertyPath().toString(), violations.getMessage());
//        }
//        errorJsonArr.add(violationMap);

        Map<String, String> bindErrorsMap = new HashMap<>();
        MVController mvController = new MVController();
        ModelAndView mav = mvController.registerUser(model);

        if (bindingResult.hasErrors()) {
            for (Object object : bindingResult.getAllErrors()) {
                FieldError fieldError = (FieldError) object;
                bindErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute("bindErrors", bindErrorsMap);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    public static ModelAndView isUserAuthenticated(ResidentUsers sessionUserObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = null;
        if (null == sessionUserObj.getEmailAddr()) {
            mav = new ModelAndView("login", "userObj", new ResidentUsers());
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    public static void modelDataLoad(Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ArrayList<Integer> flatNumberList = new ArrayList<>(Arrays.asList(101, 102, 103, 104, 105, 106, 107, 108,
                201, 202, 203, 204, 205, 206, 207, 208,
                301, 302, 303, 304, 305, 306, 307, 308,
                401, 402, 403, 404, 405, 406, 407, 408, 0));
        model.addAttribute("flatNumberList", flatNumberList);

        ArrayList<?> residentStatusList = new ArrayList<>(Arrays.asList(RESIDENT_STATUS.values()));
        model.addAttribute("residentStatusList", residentStatusList);

        ArrayList<?> userRoleList = new ArrayList<>(Arrays.asList(USER_ROLE.values()));
        model.addAttribute("userRoleList", userRoleList);

        ArrayList<?> isActiveList = new ArrayList<>(Arrays.asList(IS_ACTIVE.values()));
        model.addAttribute("isActiveList", isActiveList);

        ArrayList<?> expenseTypeList = new ArrayList<>(Arrays.asList(EXPENSE_TYPE.values()));
        model.addAttribute("expenseTypeList", expenseTypeList);

        ArrayList<?> modeOfPaymentList = new ArrayList<>(Arrays.asList(MODE_OF_PAYMENT.values()));
        model.addAttribute("modeOfPaymentList", modeOfPaymentList);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    public static Boolean hasBindingErrors(BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        Map<String, String> bindErrorsMap = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (Object object : bindingResult.getAllErrors()) {
                FieldError fieldError = (FieldError) object;
                bindErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);

            return Boolean.TRUE;
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return Boolean.FALSE;
    }
}
