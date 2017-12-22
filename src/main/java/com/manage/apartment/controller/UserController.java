package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.service.UserService;
import com.manage.apartment.validate.ResidentUserValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;
import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_USER_OBJ_LIST;

/**
 * Created by 212591727 on 3/24/2017.
 */
@RestController
@SessionAttributes({MODEL_LOGIN_USER, MODEL_USER_OBJ_LIST})
public class UserController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    UserService userService;

    @Autowired
    SuperAdminController auditTrailLog;
    @Autowired
    ResidentUserValidator residentUserValidator;
    @Autowired
    ManageMyApartmentUtil manageMyApartmentUtil;


    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createNewUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/edit/{userId}")
    public ModelAndView editUser(@PathVariable Integer userId,
                                 @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                 Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            if (userId == userSessObj.getSystemUserId()) {
                model.addAttribute(MODEL_SAME_USER_EDIT, Boolean.TRUE);
            } else {
                model.addAttribute(MODEL_SAME_USER_EDIT, Boolean.FALSE);
            }

            ResidentUsers residentUsers = userService.findOneUser(userId);
            mav = new ModelAndView(VIEW_UPDATE_USER, MODEL_UPDATE_USER_OBJ, residentUsers);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/getAllUsers")
    public ModelAndView getAllUsers(@RequestParam(value = REPORTS_PARAM) String requestParam,
                                    @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            List<ResidentUsers> userDetailsList = userService.findAllUsers();
            List<ResidentUsers> userList = new ArrayList<>();

            if (ManageMyApartmentUtil.isUserSuperAdmin(userSessObj)) {
                mav = new ModelAndView(VIEW_PROFILE_LIST, MODEL_USER_OBJ_LIST, userDetailsList);
            }

            for (ResidentUsers residentUsers : userDetailsList) {
                if (!ManageMyApartmentUtil.isUserSuperAdmin(residentUsers) &&
                        residentUsers.getAdditionalUserDetails().getIsActive()) {
                    userList.add(residentUsers);
                }
            }

            if (ManageMyApartmentUtil.isUserAdmin(userSessObj) &&
                    !ManageMyApartmentUtil.isUserSuperAdmin(userSessObj)) {
                mav = new ModelAndView(VIEW_PROFILE_LIST, MODEL_USER_OBJ_LIST, userList);
            }

            if (!ManageMyApartmentUtil.isUserAdmin(userSessObj)) {
                mav = new ModelAndView(VIEW_PROFILE_LIST, MODEL_USER_OBJ_LIST,
                        userService.findUsersByEmailAddress(userSessObj.getEmailAddr()));
            }

            mav.addObject(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            mav.addObject(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));

            if (requestParam.equals(Boolean.TRUE.toString())) {
                mav.addObject(REPORTS_PAGE, Boolean.TRUE);

                Reports reports = new Reports();
                reports.setReportsType(REPORT_DOC_TYPE.user.name());
                mav.addObject(MODEL_REPORT_OBJ, reports);
            } else {
                mav.addObject(REPORTS_PAGE, Boolean.FALSE);
            }
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/register")
    public ModelAndView registerUser(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                     Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ManageMyApartmentUtil.modelDataLoad(model);
        ModelAndView mav = new ModelAndView(VIEW_REGISTER_USER, MODEL_USER_OBJ, new ResidentUsers());

        if (userSessObj.getSystemUserId() == 0) {
            mav.addObject(MODEL_IS_SUPER_ADMIN, Boolean.FALSE);
            mav.addObject(MODEL_IS_ADMIN, Boolean.FALSE);
        } else {
            mav.addObject(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            mav.addObject(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/createUser")
    public ModelAndView invalidCreateUser(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/updateUser")
    public ModelAndView invalidUpdateUser(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/createUser")
    public ModelAndView createUser(@Valid @ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers registeringUser,
                                   BindingResult bindingResult, Model model, HttpServletRequest request) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        ManageMyApartmentUtil.modelDataLoad(model);
        ResidentUsers loggedInUser = (ResidentUsers) request.getSession().getAttribute(MODEL_LOGIN_USER);

        if (loggedInUser.getSystemUserId() == 0) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, Boolean.FALSE);
            model.addAttribute(MODEL_IS_ADMIN, Boolean.FALSE);
        } else {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(loggedInUser));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(loggedInUser));
        }

        ModelAndView mav = registerUser(loggedInUser, model);

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return mav;
        }

        List<ResidentUsers> userDbObj = userService.findByEmailAddrOrFlatNumberAndAdditionalUserDetails_IsActive(
                registeringUser.getEmailAddr(), registeringUser.getFlatNumber());

        if (0 == userDbObj.size()) {
            bindErrorsMap = residentUserValidator.residentUserValidation(registeringUser, CREATE);

            if (bindErrorsMap.size() == 0) {
                registeringUser = prepareUserObject(registeringUser, CREATE);
                userService.createUser(registeringUser);
            } else {
                mav = new ModelAndView(VIEW_REGISTER_USER, MODEL_USER_OBJ, registeringUser);
            }
        } else {
            bindErrorsMap.put(KEY_ERRORS, INVALID_USER_EXISTS);
            mav = new ModelAndView(VIEW_REGISTER_USER, MODEL_USER_OBJ, registeringUser);
        }

        displaySuccessMessage(bindErrorsMap, registeringUser.getEmailAddr(), CREATE, model, registeringUser);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/updateUser")
    public ModelAndView updateUser(@Valid @ModelAttribute(value = MODEL_UPDATE_USER_OBJ) ResidentUsers userObjData,
                                   BindingResult bindingResult, Model model, HttpServletRequest request) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        ManageMyApartmentUtil.modelDataLoad(model);
        ModelAndView mav = editUser(userObjData.getSystemUserId(), userObjData, model);

        ResidentUsers loggedInUser = (ResidentUsers) request.getSession().getAttribute(MODEL_LOGIN_USER);

        if (loggedInUser.getSystemUserId() == 0) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, Boolean.FALSE);
            model.addAttribute(MODEL_IS_ADMIN, Boolean.FALSE);
        } else {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(loggedInUser));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(loggedInUser));
        }

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return mav;
        }

        ResidentUsers userDbObj = userService.findOneUser(userObjData.getSystemUserId());
        if (null != userDbObj) {
            try {
                bindErrorsMap = residentUserValidator.residentUserValidation(userObjData, UPDATE);

                if (bindErrorsMap.size() == 0) {
                    userObjData = prepareUserObject(userObjData, UPDATE);
                    userService.createUser(userObjData);
                } else {
                    mav = new ModelAndView(VIEW_UPDATE_USER, MODEL_UPDATE_USER_OBJ, userObjData);
                }
            } catch (Exception ex) {
                bindErrorsMap.put(KEY_ERRORS, ex.getLocalizedMessage());
            }
        } else {
            bindErrorsMap.put(KEY_ERRORS, INVALID_USER);
            mav = new ModelAndView(VIEW_UPDATE_USER, MODEL_UPDATE_USER_OBJ, userObjData);
        }

        displaySuccessMessage(bindErrorsMap, userObjData.getEmailAddr(), UPDATE, model, userObjData);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return mav;
    }

    @GetMapping(value = "/deleteUser/{userId}")
    public ModelAndView deleteUser(@PathVariable int userId, @ModelAttribute(value = MODEL_LOGIN_USER)
            ResidentUsers userSessObj, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        ResidentUsers userDetails = userService.findOneUser(userId);
        if (null == mav) {
            try {
                userDetails.getAdditionalUserDetails().setIsActive(Boolean.FALSE);
                userService.createUser(userDetails);
            } catch (Exception ex) {
                bindErrorsMap.put(KEY_ERRORS, ex.toString());
            }

            displaySuccessMessage(bindErrorsMap, userDetails.getEmailAddr(), DELETE, model, userSessObj);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return getAllUsers(Boolean.FALSE.toString(), userSessObj);
    }

    @PostMapping(value = "/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute(value = MODEL_RESET_PASSWORD) ResidentUsers resetPasswordObj,
                                      Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ResidentUsers userDbObj = userService.findUsersByEmailAddress(resetPasswordObj.getEmailAddr());

        if (null == userDbObj) {
            model.addAttribute(MODEL_MESSAGE, EMAIL_ADDRESS_DOES_NOT_EXISTS);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        String encryptNewPass = ManageMyApartmentUtil.cryptWithMD5(resetPasswordObj.getPassword());
        String encryptConfirmPass = ManageMyApartmentUtil.cryptWithMD5(resetPasswordObj.getConfirm_password());

        if (encryptNewPass.equals(userDbObj.getPassword())) {
            model.addAttribute(MODEL_MESSAGE, INVALID_NEW_PASSWORD);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        if (!encryptNewPass.equals(encryptConfirmPass)) {
            model.addAttribute(MODEL_MESSAGE, INVALID_PASSWORD_MISMATCH);
            return new ModelAndView(VIEW_RESET_PASSWORD);
        }

        userDbObj.setPassword(encryptNewPass);
        userService.createUser(userDbObj);

        model.addAttribute(MODEL_MESSAGE, MessageFormat.format(SUCCESS_MSG, PASSWORD, UPDATE));

        auditTrailLog.recordAuditTrailLog(RESIDENT_USERS, userDbObj.getEmailAddr(),
                MessageFormat.format(SUCCESS_MSG, PASSWORD, UPDATE), PASSWORD.concat(UNDER_SCORE + UPDATE),
                new Timestamp(System.currentTimeMillis()));

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return new ModelAndView(VIEW_RESET_PASSWORD);
    }

    private ResidentUsers prepareUserObject(ResidentUsers registeringUser, String actionType) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        if(actionType.equals(UPDATE)) {
            UploadFile uploadFile;

            if(0 < registeringUser.getAdditionalUserDetails().getUploadFile().getTempFile().getSize()){
                uploadFile = ManageMyApartmentUtil.uploadFileDetails(registeringUser.getAdditionalUserDetails().
                        getUploadFile(), REPORT_DOC_TYPE.transact.name());
            } else {
                uploadFile = registeringUser.getAdditionalUserDetails().getUploadFile();
                uploadFile.setUpdationDate(new Timestamp(System.currentTimeMillis()));
            }

            registeringUser.getAdditionalUserDetails().setUploadFile(uploadFile);
        }

        if (actionType.equals(CREATE)) {
            if(0 < registeringUser.getAdditionalUserDetails().getUploadFile().getTempFile().getSize()){
                UploadFile uploadFile = ManageMyApartmentUtil.uploadFileDetails(registeringUser.getAdditionalUserDetails().
                        getUploadFile(), REPORT_DOC_TYPE.transact.name());
                registeringUser.getAdditionalUserDetails().setUploadFile(uploadFile);
            }

            registeringUser.getAdditionalUserDetails().setUserRole(USER_ROLE.USER.toString());
            registeringUser.getAdditionalUserDetails().setIsActive(Boolean.TRUE);

            registeringUser.setPassword(ManageMyApartmentUtil.cryptWithMD5(registeringUser.getPassword()));
        }

        registeringUser.setCreationDate(registeringUser.getCreationDate());
        registeringUser.setUpdationDate(new Timestamp(System.currentTimeMillis()));

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return registeringUser;
    }

    private void displaySuccessMessage(Map<String, String> bindErrorsMap, String emailAddress, String label,
                                       Model model,
                                       @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers loggedInUser) {
        if (bindErrorsMap.size() == 0) {
            bindErrorsMap.put(KEY_SUCCESS, MessageFormat.format(SUCCESS_MSG, emailAddress, label));
            auditTrailLog.recordAuditTrailLog(RESIDENT_USERS, loggedInUser.getEmailAddr(),
                    MessageFormat.format(SUCCESS_MSG, emailAddress, label), USER.concat(UNDER_SCORE + label),
                    new Timestamp(System.currentTimeMillis()));
        }

        model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);
    }

    public void updatePendingAmount(int transFlatNumber, float maintAmount, boolean deduct) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        List<ResidentUsers> residentUsersList;
        if (transFlatNumber == 0) {
            residentUsersList = userService.findAllUsers();
        } else {
            residentUsersList = userService.findUsersByFlatNumber(transFlatNumber);
        }

        for (ResidentUsers residentUser : residentUsersList) {
            if (residentUser.getFlatNumber() != 0 && residentUser.getAdditionalUserDetails().getIsActive()) {
                int pendingAmount = residentUser.getPendingAmount();
                if (deduct) {
                    pendingAmount -= maintAmount;
                } else {
                    pendingAmount += maintAmount;
                }

                residentUser.setPendingAmount(pendingAmount);
                userService.createUser(residentUser);
            }
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    public int getNoOfFlats(String currMonth) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        int noOfFlats = 0;

        List<ResidentUsers> residentUsersList = userService.findAllUsers();

        Date currMonthYear = ManageMyApartmentUtil.getFormattedDate(currMonth.concat("-01"));

        for (ResidentUsers residentUsers : residentUsersList) {
            Date residingSince = ManageMyApartmentUtil.getFormattedDate(residentUsers.getResidingSince());

            if (residingSince.before(currMonthYear) && residentUsers.getAdditionalUserDetails().getIsActive()
                    && !residentUsers.getAdditionalUserDetails().getUserRole().equals(USER_ROLE.SUPER_ADMIN.name())) {
                noOfFlats++;
            }
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return noOfFlats;
    }
}
