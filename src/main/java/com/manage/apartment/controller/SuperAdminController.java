package com.manage.apartment.controller;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.model.AuditTrail;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.UploadFile;
import com.manage.apartment.repository.AuditTrailRepository;
import com.manage.apartment.service.SuperAdminService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.MessageFormat;

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
                   DOC_UPLOAD_TYPE.super_admin.name()));
            mav = new ModelAndView("uploadDocuments");
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @PostMapping(value = "/uploadDocuments")
    public ModelAndView saveUplaodDocuments(@ModelAttribute(value = "updateDocumentObj") UploadFile uploadDocument,
                                            @ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj,
                                            Model model){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        ModelAndView mav = ManageMyApartmentUtil.isUserAuthenticated(userSessObj);
        if (null == mav) {
            model.addAttribute(MODEL_IS_SUPER_ADMIN, ManageMyApartmentUtil.isUserSuperAdmin(userSessObj));
            model.addAttribute(MODEL_IS_ADMIN, ManageMyApartmentUtil.isUserAdmin(userSessObj));
            uploadDocument = ManageMyApartmentUtil.uploadFileDetails(uploadDocument, DOC_UPLOAD_TYPE.super_admin.name());
            superAdminService.saveDocuments(uploadDocument);
            mav = getUploadDocumentsView(userSessObj, model);
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    @GetMapping(value = "getUploadDocuments")
    public ResponseEntity<byte[]> viewPDF(@RequestParam(value = "systemUploadFileId") int systemUploadFileId,
                                          @RequestParam(value = "docType") String docUploadType) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        System.out.println("DOC_UPLOAD_TYPE="+docUploadType);
        UploadFile uploadFile = superAdminService.getOneUploadFile(systemUploadFileId);

        ResponseEntity<byte[]> response = ManageMyApartmentUtil.retrieveFileDetails(uploadFile);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return response;
    }

    @GetMapping(value = "/deleteDocument/{systemUploadFileId}")
    public ModelAndView deleteDocument(@PathVariable(value = "systemUploadFileId") int systemUploadFileId,
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
