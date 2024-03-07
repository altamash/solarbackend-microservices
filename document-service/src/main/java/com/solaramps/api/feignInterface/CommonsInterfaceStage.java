package com.solaramps.api.feignInterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

import static com.solaramps.api.constants.MicroServiceConstants.CommonsServiceConstants.API_BASE;
import static com.solaramps.api.constants.MicroServiceConstants.CommonsServiceConstants.COMMONS_SERVICE_NAME_STAGE;

@Profile("stage")
@FeignClient(name = COMMONS_SERVICE_NAME_STAGE, path = API_BASE)
public interface CommonsInterfaceStage extends CommonsInterface {
}
