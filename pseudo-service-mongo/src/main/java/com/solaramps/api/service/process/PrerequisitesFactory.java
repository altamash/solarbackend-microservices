package com.solaramps.api.service.process;

import com.solaramps.api.service.saasmigration.MigrationPrerequisitesImpl;

public class PrerequisitesFactory {
    public static MigrationPrerequisites getAPIPrerequisites(String externalAPI) {
        switch (externalAPI) {
            case "RDBMS":
                return MigrationPrerequisitesImpl.getInstance();
            default:
                throw new IllegalStateException("Unexpected value: " + externalAPI);
        }
    }
}
