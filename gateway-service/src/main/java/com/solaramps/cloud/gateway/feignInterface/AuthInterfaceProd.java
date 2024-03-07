package com.solaramps.cloud.gateway.feignInterface;

import com.solaramps.cloud.gateway.constants.MicroServiceConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@FeignClient(name = MicroServiceConstants.SolarAmpsServiceConstants.BASE_PROD, path = MicroServiceConstants.SOLARAMPS_BASE_API)
public interface AuthInterfaceProd extends AuthInterface {
}
