package com.manage.apartment.Util;

/**
 * Created by 212591727 on 3/24/2017.
 */
public interface ManageMyApartmentConstants {
    //Dynamic Statements
    String LOGGER_ENTRY = "Entry [{0}::{1}]";
    String LOGGER_EXIT = "Exit [{0}::{1}]";
    String SUCCESS_MSG = "{0} {1} Successfully";

    //Generic words
    String USER = "User";
    String LOGIN = "Login";
    String PASSWORD = "Password";
    String CREATED = "Created";
    String UPDATED = "Updated";
    String DELETED = "Deleted";
    String TRANSACTION = "Transaction";

    //Error messages
    String INVALID_LOGIN = "Invalid credentials";
    String INVALID_EMAIL = "Email address does not exists";
    String INVALID_PASSWORD_MISMATCH = "New Password and Confirm Password do not match";
    String INVALID_USER_EXISTS = "Different user with same email address exists";
    String INVALID_USER = "User does not exists";

    //Key names
    String KEY_ERRORS = "Errors";
    String KEY_SUCCESS = "Success";

    //Model Names
    String MODEL_USER_OBJ = "userObj";
    String MODEL_ADMIN_PAGE = "adminPage";
    String MODEL_RESET_PASSWORD = "resetPassword";
    String MODEL_LOGIN_ERROR = "loginError";
    String MODEL_MESSAGE = "message";
    String MODEL_RESET_PASS_SUCCESS = "resetPasswordSuccess";
    String MODEL_UPDATE_USER_OBJ = "updateUserObj";
    String MODEL_USER_OBJ_LIST = "userObjList";
    String MODEL_BIND_ERRORS = "bindErrors";
    String MODEL_TRX_SUMM_LIST = "transactionSummaryList";
    String MODEL_NEW_TRX_OBJ = "newTransactionObj";
    String MODEL_MAINT_OBJ_LIST = "maintObjList";
    String MODEL_MAINT_OBJ = "maintObj";

    //View names
    String VIEW_HOME = "home";
    String VIEW_LOGIN = "login";
    String VIEW_REGISTER_USER = "registerUser";
    String VIEW_RESET_PASSWORD = "resetPassword";
    String VIEW_UPDATE_USER = "updateUser";
    String VIEW_PROFILE_LIST = "profileList";
    String VIEW_TRANSACTION_SUMMARY = "transactionSummary";
    String VIEW_DEFAULT_MAINT_AMOUNT = "defaultMaintAmount";

    enum USER_ROLE {ADMIN, USER}

    enum RESIDENT_STATUS {OWNER, TENANT}

    enum IS_ACTIVE {TRUE, FALSE}

    enum EXPENSE_TYPE {EXPENSE, INCOME}

    enum MODE_OF_PAYMENT {Cash, Online, Wallet}
}
