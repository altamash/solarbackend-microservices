package com.solaramps.api.constants;

/**
 * This class includes the name and API end points of other microservices that we need to communicate.
 * NOTE: WRITE EVERYTHING IN ALPHABETICAL ORDER
 */
public class MicroServiceConstants {

    public static final String BASE_API = "/api";

    public interface CommonsServiceConstants {
        String COMMONS_SERVICE_NAME = "COMMONS-SERVICE";
        String COMMONS_SERVICE_NAME_DEV = "DEV-COMMONS-SERVICE";
        String COMMONS_SERVICE_NAME_STAGE = "STAGE-COMMONS-SERVICE";
        String COMMONS_SERVICE_NAME_PREPROD = "PREPROD-COMMONS-SERVICE";
        String COMMONS_SERVICE_NAME_PROD = "PROD-COMMONS-SERVICE";
        String API_BASE = "/commons";
        String UPLOAD_FILE = "/storage/storeInContainer";
        String DELETE_FILE = "/storage/delete";
    }

}
