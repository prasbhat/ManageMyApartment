package com.manage.apartment.Util;

import com.itextpdf.text.DocumentException;
import com.manage.apartment.controller.SuperAdminController;
import com.manage.apartment.controller.MonthlyExpenseController;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.model.UploadFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 212591727 on 3/24/2017.
 */
@Component
public class ManageMyApartmentUtil implements ManageMyApartmentConstants {
    private static final Logger LOGGER = Logger.getLogger(ManageMyApartmentUtil.class);
    private static MessageDigest md;
    private static String className = "ManageMyApartmentUtil";

    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;

    public static boolean isUserAuthorized(ResidentUsers dbUser, ResidentUsers loggedInUser) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        String encryptPass = ManageMyApartmentUtil.cryptWithMD5(loggedInUser.getPassword());

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return (dbUser != null && dbUser.getEmailAddr().equals(loggedInUser.getEmailAddr()) &&
                dbUser.getPassword().equals(encryptPass) && dbUser.getAdditionalUserDetails().getIsActive());
    }

    public static boolean isUserSuperAdmin(ResidentUsers loggedInUser) {
        return (USER_ROLE.SUPER_ADMIN.name().equals(loggedInUser.getAdditionalUserDetails().getUserRole()));
    }

    public static boolean isUserAdmin(ResidentUsers loggedInUser) {
        return (USER_ROLE.ADMIN.name().equals(loggedInUser.getAdditionalUserDetails().getUserRole()) ||
                USER_ROLE.SUPER_ADMIN.name().equals(loggedInUser.getAdditionalUserDetails().getUserRole()));
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

    public static ModelAndView isUserAuthenticated(ResidentUsers sessionUserObj) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ModelAndView mav = null;
        if (null == sessionUserObj.getEmailAddr()) {
            mav = new ModelAndView(VIEW_LOGIN, MODEL_USER_OBJ, new ResidentUsers());
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return mav;
    }

    public static void modelDataLoad(Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        ArrayList<Integer> flatNumberList = new ArrayList<>(Arrays.asList(
                0, 101, 102, 103, 104, 105, 106, 107, 108,
                201, 202, 203, 204, 205, 206, 207, 208,
                301, 302, 303, 304, 305, 306, 307, 308,
                401, 402, 403, 404, 405, 406, 407, 408));
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

        ArrayList<?> genderList = new ArrayList<>(Arrays.asList(GENDER.values()));
        model.addAttribute("genderList", genderList);

        ArrayList<?> vehicleTypeList = new ArrayList<>(Arrays.asList(VEHICLE_TYPE.values()));
        model.addAttribute("vehicleTypeList", vehicleTypeList);

        ArrayList<?> ticketStatusList = new ArrayList<>(Arrays.asList(TICKET_STATUS.values()));
        model.addAttribute("ticketStatusList", ticketStatusList);

        ArrayList<?> userStatusList = new ArrayList<>(Arrays.asList(USER_DETAILS.values()));
        model.addAttribute("userStatusList", userStatusList);

        ArrayList<?> viewStatusList = new ArrayList<>(Arrays.asList(VIEW_STATUS.values()));
        model.addAttribute("viewStatusList", viewStatusList);

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

    public static Boolean printHibernateValidatorConstraints(Set<ConstraintViolation<ResidentUsers>> constraintViolations,
                                                             Model model) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        Map<String, String> bindErrorsMap = new HashMap<>();

        if (constraintViolations.size() > 0) {
            for (ConstraintViolation violations : constraintViolations) {
                bindErrorsMap.put(violations.getPropertyPath().toString(), violations.getMessage());
            }
            model.addAttribute(MODEL_BIND_ERRORS, bindErrorsMap);
            return Boolean.TRUE;
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return Boolean.FALSE;
    }

    public static UploadFile uploadFileDetails(UploadFile uploadFile, String docUploadType) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

//        UploadFile uploadFile = new UploadFile();

        try {
            MultipartFile inputFile = uploadFile.getTempFile();
            byte[] byteArr = inputFile.getBytes();

            if (null == uploadFile.getFilename() || uploadFile.getFilename().isEmpty())
                uploadFile.setFilename(inputFile.getOriginalFilename().substring(0,
                        inputFile.getOriginalFilename().lastIndexOf(STRING_PERIOD)));

//            uploadFile.setFilename(filename);
            uploadFile.setFileData(byteArr);
            uploadFile.setCreationDate(new Timestamp(System.currentTimeMillis()));
            uploadFile.setUpdationDate(new Timestamp(System.currentTimeMillis()));
            uploadFile.setDocUploadType(docUploadType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return uploadFile;
    }

    public static ResponseEntity<byte[]> retrieveFileDetails(UploadFile outputFile) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        HttpHeaders headers = new HttpHeaders();

        String filename = outputFile.getFilename();
        byte[] filePdfBytes = outputFile.getFileData();

        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(filePdfBytes, headers, HttpStatus.OK);

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return response;
    }

    public static Date getFormattedDate(String inputDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date formattedDate = new Date();
        try {
            formattedDate = formatter.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    // ----- END OF STATIC METHODS ----- //

    @Autowired
    SuperAdminController superAdminController;

    @Autowired
    MonthlyExpenseController monthlyExpenseController;

    public void recordAuditTrailLog(String tableName, String username, String log_description, String log_action,
                                    Timestamp log_time) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        superAdminController.recordAuditTrailLog(tableName, username, log_description, log_action, log_time);
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    public boolean isMonthFreezed(String monthYear) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
        return monthlyExpenseController.isMonthOpen(monthYear);
    }

    public void generateReports(HttpServletResponse response, Map<String, Object> reportConfigMap) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        try {
            String filename = getFilename(reportConfigMap.get(PDF_FILENAME).toString());
            Path tempFilePath = Paths.get(filename);

            if (!Files.exists(tempFilePath.getParent()))
                Files.createDirectories(tempFilePath.getParent());

            pdfGenaratorUtil.createPdf(reportConfigMap.get(REPORT_NAME).toString(), reportConfigMap, filename);

            if (Files.exists(tempFilePath)) {
                response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + tempFilePath.getFileName());

                Files.copy(tempFilePath, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (IOException iOEx) {
            LOGGER.error(iOEx.getMessage());
            iOEx.printStackTrace();
        } catch (DocumentException dEx) {
            LOGGER.error(dEx.getMessage());
            dEx.printStackTrace();
        }

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }

    private String getFilename(String pdfFilename) throws UnsupportedEncodingException {

        String dateStr = (new SimpleDateFormat(DATE_PATTERN)).format(new Date());
        String filePath = new File(STRING_PERIOD).getAbsolutePath();

        return filePath.substring(0, filePath.lastIndexOf(File.separator) + 1) +
                FOLDER_REPORTS + pdfFilename + dateStr + FILE_PDF_EXTENSION;
    }
}
