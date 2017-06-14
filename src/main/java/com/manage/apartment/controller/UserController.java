package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_USER_OBJ;

/**
 * Created by 212591727 on 3/24/2017.
 */
@RestController
@SessionAttributes(MODEL_USER_OBJ)
public class UserController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);
    @Autowired
    UserRepository userDao;
//    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//    private static Validator validator = validatorFactory.getValidator();
    private String className = this.getClass().getSimpleName();
    @Value("${com.manage.apartment.default.password}")
    private String defaultPassword;

    @ModelAttribute(value = MODEL_USER_OBJ)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/edit/{userId}")
    public ModelAndView editUser(@PathVariable Integer userId, @ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj,
                                 Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);
            mav = new ModelAndView(VIEW_UPDATE_USER, MODEL_UPDATE_USER_OBJ, userDao.findOne(userId));
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/getAllUsers")
    public ModelAndView getAllUsers(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj, Pageable pageable) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            System.out.println(pageable.toString());
            mav = new ModelAndView(VIEW_PROFILE_LIST, MODEL_USER_OBJ_LIST, userDao.findAll(pageable));
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/createUser")
    public ModelAndView createUser(@Valid @ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers user,
                                   BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        MVController mvController = new MVController();
        ModelAndView mav = mvController.registerUser(model);

        Map<String, String> bindErrorsMap = new HashMap<>();

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return mav;
        }

        ResidentUsers userDbObj = userDao.findByEmailAddr(user.getEmailAddr());
        if (null == userDbObj) {
            try {
                user = prepareUserObject(user);
                userDao.save(user);
            } catch (Exception ex) {
                bindErrorsMap.put(KEY_ERRORS, ex.toString());
            }
        } else {
            bindErrorsMap.put(KEY_ERRORS, INVALID_USER_EXISTS);
        }

        displaySuccessMessage(bindErrorsMap, user.getEmailAddr(), CREATED, model);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/updateUser")
    public ModelAndView updateUser(@Valid @ModelAttribute(value = MODEL_UPDATE_USER_OBJ) ResidentUsers userObjData,
                                   BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        ManageMyApartmentUtil.modelDataLoad(model);
        ModelAndView mav = editUser(userObjData.getSystemUserId(), userObjData, model);

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return mav;
        }

        ResidentUsers userDbObj = userDao.findOne(userObjData.getSystemUserId());
        if (null != userDbObj) {
            try {
                userDao.save(userObjData);
            } catch (Exception ex) {
                bindErrorsMap.put(KEY_ERRORS, ex.toString());
            }
        } else {
            bindErrorsMap.put(KEY_ERRORS, INVALID_USER);
        }

        displaySuccessMessage(bindErrorsMap, userObjData.getEmailAddr(), UPDATED, model);

        model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return mav;
    }

    @GetMapping(value = "/deleteUser/{userId}")
    public ModelAndView deleteUser(@PathVariable int userId, @ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj,
                                   Model model, Pageable pageable) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            try {
                userDao.delete(userId);
            } catch (Exception ex) {
                bindErrorsMap.put(KEY_ERRORS, ex.toString());
            }

            displaySuccessMessage(bindErrorsMap, USER, DELETED, model);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return getAllUsers(userSessObj, pageable);
    }

    private ResidentUsers prepareUserObject(ResidentUsers newUserObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        String encryptPass;

        if (newUserObj.getFlatNumber() == 0) {
            newUserObj.setUserRole(USER_ROLE.ADMIN.toString());
            encryptPass = ManageMyApartmentUtil.cryptWithMD5(USER_ROLE.ADMIN.toString().toLowerCase());
        } else {
            newUserObj.setUserRole(USER_ROLE.USER.toString());
            encryptPass = ManageMyApartmentUtil.cryptWithMD5(defaultPassword);
        }
        newUserObj.setPassword(encryptPass);
        newUserObj.setIsActive(Boolean.TRUE);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return newUserObj;
    }

    private void displaySuccessMessage(Map<String, String> bindErrorsMap, String emailAddress, String label, Model model) {
        if (bindErrorsMap.size() == 0) {
            bindErrorsMap.put(KEY_SUCCESS, MessageFormat.format(SUCCESS_MSG, emailAddress, label));
        }

        model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);
    }

    public void addPendingAmount(float defaultMaintAmount) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        List<ResidentUsers> fullUserObj = (List<ResidentUsers>) userDao.findAll();
        for (ResidentUsers residentUser : fullUserObj) {
            if (residentUser.getFlatNumber() != 0) {
                float pendingAmount = residentUser.getPendingAmount();
                pendingAmount += defaultMaintAmount;
                residentUser.setPendingAmount(pendingAmount);
                userDao.save(residentUser);
            }
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    public void updatePendingAmount(int transFlatNumber, float maintAmount) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        List<ResidentUsers> residentUsersList = userDao.findByFlatNumber(transFlatNumber);
        for (ResidentUsers residentUser : residentUsersList) {
            float pendingAmount = residentUser.getPendingAmount();
            pendingAmount -= maintAmount;
            if (pendingAmount < 0) {
                pendingAmount = 0;
            }

            residentUser.setPendingAmount(pendingAmount);
            userDao.save(residentUser);
        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }
}
