package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.DefaultMaintenance;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.DefaultMaintenanceRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_USER_OBJ;

/**
 * Created by 212591727 on 4/12/2017.
 */
@RestController
@SessionAttributes(MODEL_USER_OBJ)
public class DefaultMaintenanceController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(DefaultMaintenanceController.class);
    private String className = this.getClass().getSimpleName();
    @Autowired
    private UserController userController;

    @Autowired
    private DefaultMaintenanceRepository defaultMaintenanceRepository;

    @ModelAttribute(value = MODEL_USER_OBJ)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/defaultMaintAmount")
    public ModelAndView defaultMaintenanceAmount(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj,
                                                 Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model.addAttribute(MODEL_MAINT_OBJ_LIST, defaultMaintenanceRepository.findAll());
            model.addAttribute(MODEL_MAINT_OBJ, new DefaultMaintenance());
            mav = new ModelAndView(VIEW_DEFAULT_MAINT_AMOUNT);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/defaultAmount")
    public ModelAndView defaultMaintenanceAmount(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj,
                                                 @ModelAttribute(value = MODEL_MAINT_OBJ) DefaultMaintenance defaultMaintenance,
                                                 BindingResult bindingResult, Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return defaultMaintenanceAmount(userSessObj, model);
        }

        if (null == defaultMaintenanceRepository.findByMonthYear(defaultMaintenance.getMonthYear())) {
            defaultMaintenanceRepository.save(defaultMaintenance);
            userController.addPendingAmount(defaultMaintenance.getDefaultAmount());
        } else {
            LOGGER.error("Entry for this month already exists");
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return defaultMaintenanceAmount(userSessObj, model);
    }
}
