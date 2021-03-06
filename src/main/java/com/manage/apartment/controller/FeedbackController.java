package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.Feedback;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.service.FeedbackService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@Controller
@SessionAttributes(MODEL_LOGIN_USER)
public class FeedbackController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(FeedbackController.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    ManageMyApartmentUtil manageMyApartmentUtil;

    @Autowired
    FeedbackService feedbackService;

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/feedback")
    public ModelAndView feedbackHome(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                     Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);

        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);
            mav = new ModelAndView(FEEDBACK, MODEL_FEEDBACK_OBJ, new Feedback());
            mav.addObject(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            mav.addObject(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));

            if (ManageMyApartmentUtil.isUserSuperAdmin(userSessObj) || ManageMyApartmentUtil.isUserAdmin(userSessObj)) {
                mav.addObject(MODEL_COMPLAINTS_OBJ_LIST, feedbackService.getAllFeedback());
            } else {
                mav.addObject(MODEL_COMPLAINTS_OBJ_LIST,
                        feedbackService.getFeedbackByFlatNumber(userSessObj.getFlatNumber()));
            }

        }
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/addEditFeedback")
    public ModelAndView addEditFeedback(@Valid @ModelAttribute(value = "feedbackObj")
                                                Feedback feedback,
                                        @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                        Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        Random random = new Random();
        DateFormat date = new SimpleDateFormat(DATE_PATTERN);

        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);

            feedback.setComplaintId(date.format(new Date()) + String.valueOf(random.nextInt(99)));
            feedback.setFlatNumber(userSessObj.getFlatNumber());
            feedback.setUsername(userSessObj.getEmailAddr());

            feedbackService.createFeedback(feedback);
            mav = feedbackHome(userSessObj, model);
            mav.addObject(MODEL_MESSAGE, MessageFormat.format(SUCCESS_MSG, FEEDBACK.concat(STRING_HASH +
                            feedback.getComplaintId()), CREATE));
        }

        recordAuditEntry(userSessObj.getEmailAddr());

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    private void recordAuditEntry(String emailAddress) {
        manageMyApartmentUtil.recordAuditTrailLog(FEEDBACK, emailAddress, MessageFormat.format(SUCCESS_MSG, FEEDBACK,
                CREATE), REGISTER, new Timestamp(System.currentTimeMillis()));
    }
}
