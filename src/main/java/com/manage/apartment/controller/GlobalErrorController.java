package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.el.MethodNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;

@EnableWebMvc
@ControllerAdvice
public class GlobalErrorController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(GlobalErrorController.class);
    private String className = this.getClass().getSimpleName();
    public static final String DEFAULT_ERROR_VIEW = "error";

//    @ExceptionHandler(value = NotFoundException.class)
//    public ModelAndView errorHandlerForNotFound(HttpServletRequest req, NotFoundException ex) {
//        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
//        LOGGER.error("Object not found Exception during execution of Spring application");
//        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
//        return prepareErrorModel("Object not found", req.getRequestURL(), HttpStatus.NOT_FOUND
//                , ex.getStackTrace());
//
//    }
//
//    @ExceptionHandler(value = MethodNotFoundException.class)
//    public ModelAndView errorHandlerForMethodNotFoundExceptionException(HttpServletRequest req, Exception ex) {
//        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
//        LOGGER.error("Method Not Found Exception during execution of Spring application");
//        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
//        return prepareErrorModel("Method Not Found", req.getRequestURL(), HttpStatus.METHOD_NOT_ALLOWED
//                , ex.getStackTrace());
//    }
//
//    @ExceptionHandler(value = BadRequestException.class)
//    public ModelAndView errorHandlerForBadRequest(HttpServletRequest req, Exception ex) {
//        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
//        LOGGER.error("Bad Request Exception during execution of Spring application");
//        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
//        return prepareErrorModel("Bad Request", req.getRequestURL(), HttpStatus.BAD_REQUEST
//                , ex.getStackTrace());
//    }
//
//    @ExceptionHandler(value = UnsupportedEncodingException.class)
//    public ModelAndView errorHandlerForUnsupportedEncodingException(HttpServletRequest req, Exception ex) {
//        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
//        LOGGER.error("Unsupported Encoding Exception during execution of Spring application");
//        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
//        return prepareErrorModel("Unsupported Encoding", req.getRequestURL(), HttpStatus.UNSUPPORTED_MEDIA_TYPE
//                , ex.getStackTrace());
//    }

    @ExceptionHandler({MultipartException.class, NotFoundException.class, MethodNotFoundException.class,
            BadRequestException.class, UnsupportedEncodingException.class, Exception.class})
    public ModelAndView globalErrorHandler(HttpServletRequest req, Exception exception) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        LOGGER.error(exception.toString());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        if(exception instanceof MultipartException){
            httpStatus = HttpStatus.PAYLOAD_TOO_LARGE;
        }
        if(exception instanceof NotFoundException){
            httpStatus = HttpStatus.NOT_FOUND;
        }
        if(exception instanceof MethodNotFoundException){
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        }
        if(exception instanceof BadRequestException){
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        if(exception instanceof UnsupportedEncodingException){
            httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return prepareErrorModel(exception, req.getRequestURL(), httpStatus);
    }

    private ModelAndView prepareErrorModel(Exception exception, StringBuffer urlString, HttpStatus httpStatus){
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("exception", exception.getCause().getMessage());
        mav.addObject("url", urlString);
        mav.addObject("status", httpStatus);
        mav.addObject("timestamp", new Date().toString());
        mav.addObject("fullReport", exception.toString());
        return mav;
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory containerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return factory;
    }
}
