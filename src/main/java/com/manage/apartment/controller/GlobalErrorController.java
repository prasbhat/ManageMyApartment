package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalErrorController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(GlobalErrorController.class);
    private String className = this.getClass().getSimpleName();
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView errorHandlerForNotFound(HttpServletRequest req, Exception ex) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.error("Object not found Exception during execution of Spring application");
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return prepareErrorModel("Object not found", req.getRequestURL(), HttpStatus.NOT_FOUND
                , ex.getStackTrace());

    }

    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView errorHandlerForBadRequest(HttpServletRequest req, Exception ex) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.error("Bad Request Exception during execution of Spring application");
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return prepareErrorModel("Bad Request", req.getRequestURL(), HttpStatus.BAD_REQUEST
                , ex.getStackTrace());
    }

    @ExceptionHandler(value = UnsupportedEncodingException.class)
    public ModelAndView errorHandlerForUnsupportedEncodingException(HttpServletRequest req, Exception ex) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.error("Unsupported Encoding Exception during execution of Spring application");
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return prepareErrorModel("Bad Request", req.getRequestURL(), HttpStatus.BAD_REQUEST
                , ex.getStackTrace());
    }

    private ModelAndView prepareErrorModel(String exDescription, StringBuffer urlString, HttpStatus httpStatus,
                                           StackTraceElement[] fullReport){
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("exception", exDescription);
        mav.addObject("url", urlString);
        mav.addObject("status", httpStatus);
        mav.addObject("timestamp", new Date().toString());
        mav.addObject("fullReport", fullReport);
        return mav;
    }

}
