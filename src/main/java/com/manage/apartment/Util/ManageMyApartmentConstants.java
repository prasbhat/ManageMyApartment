package com.manage.apartment.Util;

/**
 * Created by 212591727 on 3/24/2017.
 */
public interface ManageMyApartmentConstants {
    //Dynamic Statements
    String LOGGER_ENTRY = "Entry [{0}::{1}]";
    String LOGGER_EXIT = "Exit [{0}::{1}]";
    String SUCCESS_MSG = "{0} {1} Successful";
    String MONTH_FREEZE = "Month {0} is Freezed";
    String INVALID_LENGTH = "should be between {0} and {1} characters";

    //Constants
    String STRING_OF = " of ";
    String UNDER_SCORE = "_";
    String STRING_PERIOD = ".";
    String STRING_ALL = "ALL";
    String STRING_HASH = "#";
    String STRING_EQUAL = "=";
    int NUMBER_ZERO = 0;

    //Action words
    String USER = "User";
    String LOGIN = "Login";
    String CREATE = "Create";
    String UPDATE = "Update";
    String DELETE = "Delete";
    String TRANSACTION = "Transaction";
    String FREEZE = "Freeze";
    String REGISTER = "Register";
    String UPLOAD = "Upload";

    //Error messages
    String INVALID_LOGIN = "Invalid credentials";
    String EMAIL_ADDRESS_DOES_NOT_EXISTS = "Email address does not exists";
    String INVALID_EMAIL_ADDRESS = "Please provide valid Email Address";
    String INVALID_PASSWORD_MISMATCH = "Password and Confirm Password do not match";
    String INVALID_NEW_PASSWORD = "New Password cannot be same as previous password";
    String INVALID_USER_EXISTS = "An Active User with same Email Address or Flat Number already exists";
    String INVALID_USER = "User does not exists";
    String DUPLICATE_MONTH_ENTRY = "Entry for this month already exists";
    String INVALID_DATE = "Date cannot be after current date";
    String INVALID_PHONE_NUMBER = "should contain only numbers";
    String INVALID_NO_OF_PPL = "cannot be 0";
    String INVALID_FILE_SIZE = "File size should be less the n 5 MB";

    //Key names
    String KEY_ERRORS = "Errors";
    String KEY_SUCCESS = "Success";
    String PASSWORD = "Password";
    String FILENAME = "Filename";
    String EMAIL_ADDRESS = "Email Address";
    String RESIDING_SINCE = "Residing Since";
    String DATE_OF_BIRTH = "Date Of Birth";
    String PHONE_NUMBER = "Phone Number";
    String ALT_PHONE_NUMBER = "Alternate Phone Number";
    String SECONDARY_EMAIL_ADDRESS = "Secondary Email Address";
    String OWNER_PHONE_NUMBER = "Owner Phone Number";
    String OWNER_EMAIL_ADDRESS = "Owner Email Address";
    String NUMBER_OF_PPL = "Number of people";
    String REPORT_NAME = "reportName";
    String PDF_FILENAME = "pdfFilename";
    String OBJ_DATA_LIST = "objDataList";
    String MAV = "mav";
    String TITLE_TEXT = "titleText";
    String APT_NAME = "mainTitleText";
    String REG_DTL_TXT = "regDtlText";
    String ADDR_TEXT = "addrText";

    String MD5 = "MD5";
    String FILE_PDF_EXTENSION = ".pdf";
    String DATE_PATTERN = "yyyyMMddHHmmss";
    String GET_DATE_PATTERN = "yyyy-mm-dd";
    String DATE_PATTERN_REPORT = "dd-MM-yyyy HH:mm:ss";
    String FOLDER_REPORTS = "Reports/";
    String REPORTS_PARAM = "reports";
    String REPORTS_PAGE = "reportsPage";
    String PDF_USER_REPORTS = "User_Report_";
    String PDF_TRX_REPORTS = "Trans_Summ_Report_";
    String PDF_PROJECTED_SUMMARY_REPORTS = "Prj_Summ_Report_";
    String ATTACHMENT_FILENAME = "attachment; filename=";
    String INLINE_FILENAME = "inline;filename=";
    String SET_CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
    String PRINT_DATE = "printDate";
    String PRINT_NAME = "printName";

    //Table (Entity) Names
    String RESIDENT_USERS = "ResidentUsers";
    String TRANSACTION_SUMMARY = "TransactionSummary";
    String PROJECTED_EXPENSE_SUMMARY = "ProjectedExpenseSummary";
    String FREEZED_MONTH = "MonthlyExpense";
    String UPLOAD_FILE = "UploadFile";
    String FEEDBACK = "Feedback";

    //Model Names
    String MODEL_LOGIN_USER = "loggedInUser";
    String MODEL_USER_OBJ = "userObj";
    String MODEL_IS_ADMIN = "isAdmin";
    String MODEL_IS_SUPER_ADMIN = "isSuperAdmin";
    String MODEL_RESET_PASSWORD = "resetPassword";
    String MODEL_LOGIN_ERROR = "loginError";
    String MODEL_MESSAGE = "message";
    String MODEL_UPDATE_USER_OBJ = "updateUserObj";
    String MODEL_USER_OBJ_LIST = "userObjList";
    String MODEL_BIND_ERRORS = "bindErrors";
    String MODEL_TRX_SUMM_LIST = "transactionSummaryList";
    String MODEL_TRX_SUMM_HIST_LIST = "transactionSummaryHistList";
    String MODEL_NEW_TRX_OBJ = "newTransactionObj";
    String MODEL_REPORT_OBJ = "reportObj";
    String MODEL_IS_REPORT_TRANSACT = "isReportTransact";
    String MODEL_PRJ_EXP_SUMM_OBJ = "prjExpSummObj";
    String MODEL_PRJ_EXP_SUMM_OBJ_LIST = "prjExpSummObjList";
    String MODEL_PRJ_EXP_SUMM_HIST_OBJ_LIST = "prjExpSummHistObjList";
    String MODEL_SAME_USER_EDIT = "sameUserEdit";
    String MODEL_FULL_MONTH_LIST = "fullMonthList";
    String MODEL_CURR_MONTH_YEAR = "currYearMonth";
    String MODEL_TOTAL_EXP_OBJ = "totalExpenseObj";
    String MODEL_FREEZED_MONTH_OBJ = "freezeMonthObj";
    String MODEL_IS_PROJECTED_ALLLOWED = "isProjectedAllowed";
    String MODEL_AUDIT_TRIAL_OBJ_LIST = "auditTrailObjList";
    String MODEL_FEEDBACK_OBJ = "feedbackObj";
    String MODEL_COMPLAINTS_OBJ_LIST = "complaintsObjList";
    String MODEL_SYSTEM_UPLOAD_FILE_ID = "systemUploadFileId";
    String MODEL_DOC_TYPE = "docType";

    //View names
    String VIEW_HOME = "home";
    String VIEW_LOGIN = "login";
    String VIEW_REGISTER_USER = "registerUser";
    String VIEW_RESET_PASSWORD = "resetPassword";
    String VIEW_UPDATE_USER = "updateUser";
    String VIEW_PROFILE_LIST = "profileList";
    String VIEW_TRANSACTION_SUMMARY = "transactionSummary";
    String VIEW_MONTHLY_REPORT = "monthlyReport";
    String VIEW_TRX_SUMM_REPORT = "transactionSummaryReport";
    String VIEW_PROJECTED_EXPENSE_SUMMARY = "projectedExpenseSummary";
    String VIEW_MONTHLY_EXPENSE = "monthlyExpense";
    String VIEW_USER_REPORTS = "userReports";
    String VIEW_PROJECT_SUMMARY_REPORTS = "projectedSummaryReport";
    String VIEW_AUDIT_TRIAL_LOGS = "auditTrailLogs";
    String VIEW_FEEDBACK = "feedback";

    //Enums
    enum USER_ROLE {SUPER_ADMIN, ADMIN, USER}

    enum RESIDENT_STATUS {OWNER, TENANT}

    enum IS_ACTIVE {TRUE, FALSE}

    enum EXPENSE_TYPE {EXPENSE, INCOME}

    enum MODE_OF_PAYMENT {CASH, ONLINE}

    enum GENDER {MALE,FEMALE}

    enum VEHICLE_TYPE {TWO_WHEELER, FOUR_WHEELER}

    enum TICKET_STATUS {NEW, OPEN, IN_PROGRESS, ON_HOLD, RESOLVED, CLOSED}

    enum USER_DETAILS {FULL_USER, SINGLE_USER}

    enum VIEW_STATUS {VIEW, DOWNLOAD}

    enum REPORT_DOC_TYPE {transact, project, user, super_admin}
}
