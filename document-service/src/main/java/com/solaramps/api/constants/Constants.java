package com.solaramps.api.constants;

public interface Constants {

    int PAGE_SIZE = 15;

    interface Message {
        interface Service {
            interface CRUD {
                String SAVE_SUCCESS = "%s saved successfully";
                String UPDATE_SUCCESS = "%s with id %d updated successfully";
                String DELETE_SUCCESS = "%s with id %d deleted!";
                String DELETE_ALL_SUCCESS = "All %s deleted!";
                String NOT_FOUND = "%S with primary key %s not found.";
                String ID_REQUIRED = "Primary key is required.";
                static String notFound(String className, Long id) {
                    return String.format(NOT_FOUND, className, id);
                }
            }
        }
    }

    final class BILLING_CODES {
        public static final String ABCRE = "ABCRE";
    }

    final class DISCOUNT_RATE_CODES {
        public static final String DSC = "DSC";
        public static final String DSCP = "DSCP";
    }

    final class JASPER_REPORTS {
        public static final String FILE_EXTENSION_PDF = ".pdf";
        public static final String FILE_EXTENSION_JASPER = ".jasper";
        public static final String FILE_EXTENSION_JRXML = ".jrxml";
    }

    final class SUBSCRIPTION_TERMINATION_STATUS {
        public static final String BILL_HEAD_STATUS_DISCONTINUED = "DISCONTINUED";
        public static final String CUSTOMER_SUBSCRIPTION_STATUS_ENDED = "ENDED";
        public static final String CUSTOMER_SUBSCRIPTION_MAPPING_STATUS_ROLL = "NO";
        public static final String TERMINATION_CANCEL = "CANCEL";
    }

    final class SUBSCRIPTION_TERMINATION_RATE_CODES {
        public static final String ROLL = "ROLL";
        public static final String ROLL_DT = "ROLLDT";
    }

    final class CUSTOMER_SUBSCRIPTION_STATUS {
        public static final String SCHEDULED = "SCHEDULED";
        public static final String ACTIVE = "ACTIVE";
    }

    final class TERMINATION_TYPE {
        public static final String ADHOC = "ADHOC";
        public static final String AUTO = "AUTO";
    }

    final class INVERTER_TYPES {
        public static final Long SOLAX_PRODUCTION_RESIDENTIAL = 7l;
        public static final Long SOLAX_PRODUCTION_COMMERCIAL = 8l;
        public static final Long SOLIS_POWER_RESIDENTIAL = 9l;
        public static final Long SOLIS_POWER_COMMERCIAL = 10l;
        public static final Long GOODWE_POWER_RESIDENTIAL = 12l;
        public static final Long GOODWE_POWER_COMMERCIAL = 11l;

    }
    final class MONITOR_PLATFORM {
        public static final String SOLAX = "SOLAX";
        public static final String SOLIS = "SOLIS";
        public static final String GOODWE = "GOODWE";
    }
    final class RATE_CODES {
        public static final String LATITUDE = "LAT";
        public static final String LONGITUDE = "LON";
        public static final String INVERTER_NUMBER = "INVRT";
        public static final String LAST_MAINTENANCE_DT="LMNTDT";
        public static final String MAINTENANCE_INTERVAL="MNTID";

    }

    final class PROJECT_DEPENDENCIES_RELATED_AT {
        public static final String RELATED_PROJECT = "related.project";
        public static final String RELATED_ACTIVITY = "related.activity";
        public static final String RELATED_TASK = "related.task";
    }

    final class PROJECT_DEPENDENCIES_TYPE {
        public static final String FINISH_START = "Finish-Start";
        public static final String START_START = "Start-Start";
        public static final String RELATED = "Related";
        public static final String FS = "FS";
        public static final String SS = "SS";
    }

    final class PROJECT_DEPENDENCIES_DIRECTIONS {
        public static final String FORWARD = "Forward";
        public static final String REVERSE = "Reverse";
    }

    final class GOODWE_API_HEADER_VALUES {
        public static final String client = "ios";
        public static final String version = "v2.1.0";
        public static final String language = "en";
    }

    final class PAYMENT_MODES {
        public static final String cash = "CASH";
        public static final String cheque = "CHEQUE";
        public static final String ach = "ACH";
        public static final String credit_card = "CREDIT CARD";

    }

    final class INVOICE_STATUS{
        public static final String paid_unreconciled = "PAID-UNRECONCILED";
        public static final String paid_reconciled ="PAID-RECONCILED";
    }

    final class PAYMENT_STATUS{
        public static final String paid = "PAID";
        public static final String failed ="FAILED";
        public static final String completed ="COMPLETED";
        public static final String inprogress ="IN-PROGRESS"; // user for line item that's going to reverse
        public static final String reversal ="REVERSAL"; // its against the reversed entry [ it will hve payment ref id]
        public static final String reversed ="REVERSED"; // for which we sent request of reversal
    }

    final class PAYMENT_DTL_DOC_REF_TYP{
        public static final String PAYMENT_TRANSACTION_DETAIL = "PTDETAIL";

    }

    final class MessageTypes{
        public static final String ERROR = "ERROR";
        public static final String WARNING = "WARNING";
        public static final String MESSAGE = "MESSAGE";

    }
    final class NEW_USER_CONSTANTS {
        //DOC_REF_TYP
        public static final String REGISTER_NEW_USER = "REGNEWUSER";
        public static final String PROFILE = "PROFILE_PHOTO.png";
        public static final String BUSINESS_LOGO = "BUSINESS_LOGO.png";
        public static final String CUSTOMER_TYPE = "Customer Type";
    }

    final class LOCATION_TYPE {
        public static final String SITE = "Site";
        public static final String BILLING = "Billing";
        public static final String MAILING = "Mailing";
        public static final String DEFAULT_ADDRESS = "Default_Address";

    }
    final  class LOCATION_CATEGORY_CONSTANTS{
        public static final String ENTITY = "ENTITY";
        public static final String USER = "USER";

    }

}

