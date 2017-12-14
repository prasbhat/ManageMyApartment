package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.AuditTrail;
import com.manage.apartment.model.Reports;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.repository.AuditTrailRepository;
import com.manage.apartment.service.SuperAdminService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

/**
 * Created by 212591727 on 4/12/2017.
 */
@RestController
@SessionAttributes(MODEL_LOGIN_USER)
public class SuperAdminController implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(SuperAdminController.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    AuditTrailRepository auditTrailRepository;

    @Autowired
    SuperAdminService superAdminService;

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createUserObj() {
        return new ResidentUsers();
    }

    @GetMapping(value = "/getAuditTrailLog")
    public ModelAndView getAuditTrialLogReport(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                               Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            model.addAttribute(MODEL_AUDIT_TRIAL_OBJ_LIST, auditTrailRepository.findAllByOrderByLogTimeDesc());
            mav = new ModelAndView(VIEW_AUDIT_TRIAL_LOGS);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "/uploadDocuments")
    public ModelAndView getUploadDocumentsView(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                               Model model){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            model.addAttribute("updateDocumentObj", new UploadFile());
           model.addAttribute("updateDocumentObjList", superAdminService.getUploadFileByDocUploadType(
                   REPORT_DOC_TYPE.super_admin.name()));
            mav = new ModelAndView("uploadDocuments");
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/uploadDocuments")
    public ModelAndView saveUplaodDocuments(@ModelAttribute(value = "updateDocumentObj") UploadFile uploadDocument,
                                            @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                            Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);

        if (null == mav) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            uploadDocument = ManageMyApartmentUtil.uploadFileDetails(uploadDocument, REPORT_DOC_TYPE.super_admin.name());
            superAdminService.saveDocuments(uploadDocument);
            recordAuditTrailLog(UPLOAD_FILE, userSessObj.getEmailAddr(),
                    MessageFormat.format(SUCCESS_MSG,uploadDocument.getFilename(),UPLOAD), UPLOAD,
                    new Timestamp(System.currentTimeMillis()));
            mav = getUploadDocumentsView(userSessObj, model);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "getUploadDocuments")
    public ResponseEntity<byte[]> viewPDF(@RequestParam(value = MODEL_SYSTEM_UPLOAD_FILE_ID) int systemUploadFileId) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        UploadFile uploadFile = superAdminService.getOneUploadFile(systemUploadFileId);

        ResponseEntity<byte[]> response = ManageMyApartmentUtil.retrieveFileDetails(uploadFile);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return response;
    }

    @GetMapping(value = "/deleteDocument/{systemUploadFileId}")
    public ModelAndView deleteDocument(@PathVariable(value = MODEL_SYSTEM_UPLOAD_FILE_ID) int systemUploadFileId,
                                       @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                       Model model){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            superAdminService.deleteDocuments(systemUploadFileId);
            mav = getUploadDocumentsView(userSessObj, model);
        }

        recordAuditTrailLog(UPLOAD_FILE, userSessObj.getEmailAddr(),
                MessageFormat.format(SUCCESS_MSG,MODEL_SYSTEM_UPLOAD_FILE_ID,DELETE), DELETE,
                new Timestamp(System.currentTimeMillis()));

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

   public void recordAuditTrailLog(String tableName, String username, String log_description, String log_action,
                                   Timestamp log_time){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setTableName(tableName);
        auditTrail.setUsername(username);
        auditTrail.setLogDescription(log_description);
        auditTrail.setLogAction(log_action);
        auditTrail.setLogTime(log_time);

        auditTrailRepository.save(auditTrail);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }
}
