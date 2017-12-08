package com.manage.apartment.validate;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.Util.ManageMyApartmentUtil;
import com.manage.apartment.controller.UserController;
import com.manage.apartment.model.ResidentUsers;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 212591727 on 8/18/2017.
 */
@Component
public class ResidentUserValidator implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);
    private String className = this.getClass().getSimpleName();

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

//    private static String MOBILE_PATTERN = "\\d{10,15}";
    private static String MOBILE_PATTERN = "^[0-9 ()+-]{10,15}$";

    private String currentDateStr = new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);

    Map<String, String> bindErrorsMap = new HashMap<>();

    public Map<String, String> residentUserValidation(ResidentUsers registeringUser, String getActionType){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));

        bindErrorsMap = new HashMap<>();

        if(CREATE.equals(getActionType)){
            validatePassword(registeringUser.getPassword(),registeringUser.getConfirm_password());
            validateFilename(registeringUser.getAdditionalUserDetails().
                    getUploadFile().getTempFile().getOriginalFilename());
        }

        validateEmailAddress(registeringUser.getEmailAddr(), EMAIL_ADDRESS);
        validatePhoneNumber(registeringUser.getPhoneNbr(), PHONE_NUMBER);

        String altPhoneNbr = registeringUser.getAdditionalUserDetails().getAltPhoneNbr();
        if(!altPhoneNbr.isEmpty()){
            validatePhoneNumber(altPhoneNbr, ALT_PHONE_NUMBER);
        }

        String secEmailAddr = registeringUser.getAdditionalUserDetails().getSecondEmailAddr();
        if(!secEmailAddr.isEmpty()){
            validateEmailAddress(secEmailAddr, SECONDARY_EMAIL_ADDRESS);
        }

        if(null != registeringUser.getOwnerDetails()){
            String ownerPhoneNumber = registeringUser.getOwnerDetails().getOwnerPhoneNbr();
            if(!ownerPhoneNumber.isEmpty())
                validatePhoneNumber(ownerPhoneNumber, OWNER_PHONE_NUMBER);

            String ownerEmailAddress = registeringUser.getOwnerDetails().getOwnerEmailAddr();
            if(!ownerEmailAddress.isEmpty())
                validateEmailAddress(ownerEmailAddress, OWNER_EMAIL_ADDRESS);
        }

        validateDate(registeringUser.getResidingSince(), RESIDING_SINCE);
        validateDate(registeringUser.getDateOfBirth(), DATE_OF_BIRTH);

        validateNoOfPpl(registeringUser.getAdditionalUserDetails().getNoOfPpl());

        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));

        return bindErrorsMap;
    }

    private void validateNoOfPpl(int noOfPpl) {
        if(noOfPpl == 0){
            bindErrorsMap.put(NUMBER_OF_PPL, INVALID_NO_OF_PPL);
        }
    }

    private void validatePhoneNumber(String phoneNbr, String label) {
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(phoneNbr);

        if(!matcher.matches()){
            bindErrorsMap.put(label, INVALID_PHONE_NUMBER);
        }
    }

    private void validateDate(String dateInString, String label){
        Date residingSinceDate = ManageMyApartmentUtil.getFormattedDate(dateInString);
        Date currentDate = ManageMyApartmentUtil.getFormattedDate(currentDateStr);

        if(residingSinceDate.after(currentDate)){
            bindErrorsMap.put(label, INVALID_DATE);
        }
    }

    private void validateEmailAddress(String emailAddr, String label) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailAddr);

        if(!matcher.matches()){
            bindErrorsMap.put(label, INVALID_EMAIL_ADDRESS);
        }

    }

    private void validateFilename(String originalFilename) {
        if(originalFilename.length() > 24){
            bindErrorsMap.put(FILENAME, MessageFormat.format(INVALID_LENGTH, 1, 20));
        }
    }

    private void validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            bindErrorsMap.put(PASSWORD, INVALID_PASSWORD_MISMATCH);
        }

        if (password.length() < 8 || password.length() > 18){
            bindErrorsMap.put(PASSWORD, MessageFormat.format(INVALID_LENGTH, 8, 18));
        }
    }

}
