package com.solaramps.cloud.gateway.feignInterface;

import com.solaramps.cloud.gateway.constants.MicroServiceConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@Profile("local")
@FeignClient(name = MicroServiceConstants.SolarAmpsServiceConstants.BASE, path = MicroServiceConstants.SOLARAMPS_BASE_API)
public interface AuthInterfaceLocal extends AuthInterface {
}
