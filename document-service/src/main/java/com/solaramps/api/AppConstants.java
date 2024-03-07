package com.solaramps.api;

public interface AppConstants {

    String CONTENT_TYPE = "text/plain";
    String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";


    String PROFILE_DEVELOPMENT = "dev";
    String PROFILE_STAGING = "stage";
    String PROFILE_PRE_PRODUCTION = "preprod";
    String PROFILE_PRODUCTION = "prod";
    String API_DATABASE_PREFIX = "jdbc:mysql://";

    //TrueUp Reporting
    String CSGR = "CSGR";
    String CSGF = "CSGF";
    String PDF = "PDF";
    String TRUE_UP_PATH = "/report/trueups/";
    String TRUEUP = "2021TrueUp";
    String MIME_TYPE_PDF = ".pdf";
    String TRUE_UP_SUBJECT = "2021 Annual True Up for Your Community Solar Subscription with Novel Energy";

    //Batch
    Long PUBLISH_INVOICE = 4L;
    Long ROLL_OVER_ALL = 5L;
    Long AUTO_SUBSCRIPTION_TERMINATION_ID = 6L;
    Long  ADHOC_SUBSCRIPTION_TERMINATION_ID = 7L;
    Long BILLING_CREDITS = 9L;
    Long NPVBatch = 10L;
    Long LIFETIME_CALCULATION = 11L;
    Long STAVEM_THROUGH_CSG = 12L;
    Long STAVEM_ROLES = 13L;
    Long HOURS_CALCULATION = 14L;
    Long AGING_REPORT = 15L;
    Long SCHEDULED_INVOICING_ID = 16L;
    Long ADD_MONITOR_READINGS_ID = 18L;
    String BILLING_CREDITS_STRING = "BillingCredits";
    String STAVEM_THROUGH_CSG_STRING = "StavemThrough";
    String STAVEM_ROLES_STRING = "StavemRoles";
    String XCEL_BILLING_CREDITS_IMPORT_JOB = "XCEL_BILLING_CREDITS_IMPORT";
    String ADHOC_SUBSCRIPTION_TERMINATION = "ADHOC_SUBSCRIPTION_TERMINATION";
    String AUTO_SUBSCRIPTION_TERMINATION = "AUTO_SUBSCRIPTION_TERMINATION";
    String ADD_MONITOR_READINGS = "ADD_MONITOR_READINGS";
    String BILLING_BY_TYPE = "SCHEDULED_INVOICING";
    String ROLL_OVER = "ROLL_OVER_ALL";
    String INVOICE_PDF_BATCH_JOB = "INVOICE_PDF";
    String STAVEM_THROUGH_CSG_IMPORT_JOB = "STAVEM_THROUGH_CSG_IMPORT";
    String STAVEM_ROLES_IMPORT_JOB = "STAVEM_ROLES_IMPORT";
    String STAVEM_ROLES_IMPORT = "STAVEM_ROLES_IMPORT";
    String BILLING_CREDITS_PATH = "/billing/credits/csv";
    String INVOICE_REPORT_PATH = "/report/invoice/output_format/pdf";
    String PROJECT_DOCUMENT_PATH = "/project/documentation";
    String PROJECT_DELETED_DOCUMENT_PATH = "/project/documentation/deleted_documents";
    String STAVEM_THROUGH_CSG_PATH = "/stavem/csv";
    String STAVEM_ROLES_PATH = "/stavem/roles/csv";
    String MIME_TYPE_CSV = ".csv";
    String CUSTOMER_SUPPORT_PATH = "/customer/support";

    String CONTRACT_FILE_PATH = "/contract";

    String BATCH_EMAIL_SUBJECT_RUNTIME = "RUNTIME EXCEPTION";
    String BATCH_EMAIL_SUBJECT_ADD_SCHEDULE = "ADD SCHEDULE EXCEPTION";
    String BATCH_EMAIL_SUBJECT_JOB_MARKED_ABANDONED = "ABANDONED JOBS";
    String BATCH_EMAIL_SUBJECT_JOB_MARKED_RESTARTED = "RESTARTED JOBS";
    String BATCH_EMAIL_SUBJECT_CRON_INVALID = "INVALID CRON EXPRESSION";
    String BATCH_EMAIL_SUBJECT_JOB_SCHEDULED = "JOB SCHEDULED";
    String BATCH_EMAIL_SUBJECT_JOB_SCHEDULE_DYNAMIC = "DYNAMIC SCHEDULE JOB";

    String PAYMENT_DETAIL_PATH = "/payment/detail";


    /**
     * Email Constants
     */
    String TESTNA_TOEMAIL = "testna@solarinformatics.com";
    String BCC_NOVEL = "invoicing@novelenergy.biz";
    String SOLAR_TEST_EMAIL = "testna@solarinformatics.com";

    /**
     * SendGrid
     * Dynamic Email Template ID
     * True Ups
     * https://mc.sendgrid.com/dynamic-templates/d-d0e1c8b8965e4f829c2eb09686eca504/version/963930fc-f610-4221-9615
     * -27d8baffd338/editor
     */
    String TRUE_UP_ARR_EMAIL_TEMPLATE = "d-d0e1c8b8965e4f829c2eb09686eca504";
    String TEST_EMAIL_TEMPLATE = "d-6c5044ab11ad4214aa8b3840fe26988f";

    /**
     * SendGrid
     * Dynamic Email Template ID
     * True Ups
     * https://mc.sendgrid.com/dynamic-templates/d-c6dd7b41d9024fa8b573910c57d37f00/version/a75221e4-8004-4956-9beb
     * -f1e5e8238581/editor
     */
    String TRUE_UP_VOS_EMAIL_TEMPLATE = "d-c6dd7b41d9024fa8b573910c57d37f00";


    /**
     * EventTypes
     */
    String EMAIL_EVENT = "EMAIL";

    /**
     * EventTypes
     */
    String BATCH_EMAIL_NOTIFICATION = "m.shariq@solarinformatics.com";
    String MONITOR_API_TO_EMAIL_NOTIFICATION = "muneeb.butt@solarinformatics.com";
    String MONITOR_API_CC_EMAIL_NOTIFICATION = "abdullah.masood@solarinformatics.com";
    String MONITOR_API_CC2_EMAIL_NOTIFICATION = "sana.siraj@solarinformatics.com";

    /**
     * SendGrid
     * Dynamic Email Template ID
     * Auto Termination Notification
     */
    String AUTO_TERMINATION_NOTIFICATION_TEMPLATE = "d-2c9783ec05204ff1a983b85f6a3d3825";

    String JOB_SUCCESS_SUBMISSION = "Your billing job request is successfully submitted. It may take several minutes " +
            "to execute depending on data.";

    /**
     * Tenant schema table overriding saas schema table must start at this value plus one
     */
    Long SAAS_RESERVED_AUTO_INCREMENT = 10000l;

    String LIFETIME_SAVINGS_CODE = "ABSAV";
    String LIFETIME_PRODUCTION_CODE = "MPA";
    Double TPLMULTIPLIER = 0.0117;
    String TPF = "TPF";

    /**
     * Scheduled Jobs
     * STATE
     */
//    String QUEUED = "QUEUED";
    String SCHEDULED = "SCHEDULED";
    String RUNNING = "RUNNING";
    String ABANDONED = "ABANDONED";
    String RESTARTED = "RESTARTED";


    /**
     * Scheduled Jobs
     * ExecutionParameters
     */
    String STATUS_ACTIVE = "ACTIVE";
    String STATUS_INACTIVE = "INACTIVE";
    String SCHEDULED_INVOICING = "SCHEDULED_INVOICING";
    String SCHEDULING_INVOICE = "SCHEDULING_INVOICE";

    //INVOICING
    String subscriptionCode = "subscriptionCode";
    String subscriptionRateMatrixIdsCSV = "subscriptionRateMatrixIdsCSV";
    String billingMonth = "billingMonth";
    String jobName = "jobName";
    String date = "date";
    String type = "type";
    String compKey = "compKey";

    //POWER_MONITORING
    String subscriptionType_PW = "subscriptionType";
    String jobManagerTenantId = "jobManagerTenantId";

    /**
     * ProjectHead
     * ProjectDetail
     * Constants
     */
    String PROJECT_LEVEL = "PROJECT";
    String ACTIVITY_LEVEL = "ACTIVITY";
    String TASK_LEVEL = "TASK";
    String CREATE = "CREATE";
    String UPDATE = "UPDATE";
    String PROJECT_MANAGER = "Project Manager";


    /**
     * Billing Constants
     */
    String INVOICE_REPORT_ID = "INVRPTID";
    String PAYMENT_INITIAL_MSG = "Processing....";


    /**
     * SkipFlag
     * BillingHead
     */
    Long SKIPPED = 1L;
    Long UNSKIPPED = 0L;

    /**
     * Contracts
     */
    String ACTIVE_STATUS = "ACTIVE";
    String INACTIVE_STATUS = "INACTIVE";
    String ERROR_MSG_UPGRADE_ACCT = "Please upgrade your account";

    final class ProjectBGColors{
        public static final String projectDefault="#FCF0CF";
        public static final String activityDefault="#CFFCFA";
        public static final String taskDefault="#EDEDED";
        public static final String onCompletion="#E6FFDE";
    }

    //register User
    String REGISTER_NEW_USER_PATH ="/user/";
    String REGISTER_NEW_USER_PROFILE_PATH ="profile/";
    String REGISTER_NEW_USER_BUSINESS_INFO_PATH ="business/";
    String REGISTER_NEW_USER_BUSINESS_LOGO_PATH ="businessLogo/";


}
