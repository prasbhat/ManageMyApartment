package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.TransactionSummary;
import com.manage.apartment.repository.TransactionSummaryRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_USER_OBJ;

/**
 * Created by 212591727 on 4/3/2017.
 */
@RestController
@SessionAttributes(MODEL_USER_OBJ)
public class TransactionSummaryController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(TransactionSummaryController.class);
    private String className = this.getClass().getSimpleName();
    @Autowired
    private TransactionSummaryRepository transactionSummaryRepository;

    @Autowired
    private UserController userController;

    @ModelAttribute(value = MODEL_USER_OBJ)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    //    @GetMapping(value = "/getTransactionHistory/{userId}")
    public ModelAndView getOneTransaction(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userObj);
        if (null == mav) {
            mav = new ModelAndView(VIEW_HOME,
                    MODEL_TRX_SUMM_LIST, transactionSummaryRepository.findByFlatNumber(userObj.getFlatNumber()));
            mav.addObject(MODEL_USER_OBJ, userObj);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/transactionSummary")
    public ModelAndView transactionSummaryHome(@ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj,
                                               Model model, Pageable pageable) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            ManageMyApartmentUtil.modelDataLoad(model);
//            model.addAttribute(MODEL_TRX_SUMM_LIST, transactionSummaryRepository.findAll());

            model.addAttribute(MODEL_TRX_SUMM_LIST, transactionSummaryRepository.findAll(pageable));
            model.addAttribute(MODEL_ADMIN_PAGE, Boolean.TRUE);
            model.addAttribute(MODEL_NEW_TRX_OBJ, new TransactionSummary());
            mav = new ModelAndView(VIEW_TRANSACTION_SUMMARY, MODEL_USER_OBJ, userSessObj);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/addTransaction")
    public ModelAndView addTransactionSummary(@ModelAttribute(value = MODEL_NEW_TRX_OBJ) TransactionSummary transactionSummaryObj,
                                              @ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userSessObj,
                                              BindingResult bindingResult, Model model, Pageable pageable) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        if (ManageMyApartmentUtil.hasBindingErrors(bindingResult, model)) {
            return transactionSummaryHome(userSessObj, model, pageable);
        }

        if (transactionSummaryObj.getFlatNumber() != 0) {
            userController.updatePendingAmount(transactionSummaryObj.getFlatNumber(), transactionSummaryObj.getAmount());
        }

        transactionSummaryRepository.save(transactionSummaryObj);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return transactionSummaryHome(userSessObj, model, pageable);
    }

    @GetMapping(value = "/deleteTransaction/{transactionId}")
    public ModelAndView deleteTransaction(@PathVariable int transactionId,
                                          @ModelAttribute(value = MODEL_USER_OBJ) ResidentUsers userObj,
                                          Model model, Pageable pageable) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        Map<String, String> bindErrorsMap = new HashMap<>();

        try {
            transactionSummaryRepository.delete(transactionId);
        } catch (Exception ex) {
            bindErrorsMap.put(KEY_ERRORS, ex.toString());
        }

        if (bindErrorsMap.size() == 0) {
            bindErrorsMap.put(KEY_SUCCESS, MessageFormat.format(SUCCESS_MSG, TRANSACTION, DELETED));
        }

        model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return transactionSummaryHome(userObj, model, pageable);
    }
}
