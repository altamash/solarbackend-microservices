package com.solaramps.api.service.saasmigration;

import com.solaramps.api.service.process.APIConstants;
import com.solaramps.api.service.process.APIConstantsImpl;
import com.solaramps.api.service.process.MigrationPrerequisites;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MigrationPrerequisitesImpl implements MigrationPrerequisites {
    private Map<String, APIConstants> constants = new HashMap<>();
    private static MigrationPrerequisitesImpl epvMonitorPrerequisites;

    private MigrationPrerequisitesImpl() {
        constants.put("SIGN_IN", APIConstantsImpl.builder().method(HttpMethod.POST)
                .url("https://bestage.azurewebsites.net/solarapi/saas/signin").build());
        constants.put("SIGN_IN_TENANT", APIConstantsImpl.builder().method(HttpMethod.POST)
                .url("https://bestage.azurewebsites.net/solarapi/signin").build());
        constants.put("SIGN_IN_TENANT_PROD", APIConstantsImpl.builder().method(HttpMethod.POST)
                .url("https://siprodbeapi.azurewebsites.net/solarapi/signin").build());
        constants.put("FIND_ALL_MEASURES_PROD", APIConstantsImpl.builder().method(HttpMethod.GET)
                .url("https://siprodbeapi.azurewebsites.net/solarapi/saas/measureDefinition").build());
        constants.put("FIND_ALL_MEASURES_STAGE", APIConstantsImpl.builder().method(HttpMethod.GET)
                .url("https://bestage.azurewebsites.net/solarapi/saas/measureDefinition").build());
        constants.put("VARIANTS_BY_CODE", APIConstantsImpl.builder().method(HttpMethod.GET)
                .url("https://siprodbeapi.azurewebsites.net/solarapi/subscription/subscriptionRateMatrixHead/subscriptionCode/").build());
        constants.put("MEASURES_BY_VARIANT", APIConstantsImpl.builder().method(HttpMethod.GET)
                .url("https://siprodbeapi.azurewebsites.net/solarapi/subscription/subscriptionRateMatrixHead/getMeasure/").build());
        constants.put("SUBSCRIPTION_RATE_MATRIX", APIConstantsImpl.builder().method(HttpMethod.GET)
                .url("https://bestage.azurewebsites.net/solarapi/subscription/subscriptionRateMatrixHead").build());
         constants.put("SUBS_RATE_MATRIX_DETAIL", APIConstantsImpl.builder().method(HttpMethod.GET)
                .url("https://siprodbeapi.azurewebsites.net/solarapi/subscription/subscriptionRateMatrixDetail").build());

    }

    public static MigrationPrerequisitesImpl getInstance() {
        if (MigrationPrerequisitesImpl.epvMonitorPrerequisites == null) {
            epvMonitorPrerequisites = new MigrationPrerequisitesImpl();
        }
        return epvMonitorPrerequisites;
    }

}
