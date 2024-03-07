package com.solaramps.cloud.gateway.feignInterface;

import com.solaramps.cloud.gateway.constants.MicroServiceConstants;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


public interface AuthInterface {

    @RequestMapping(value = MicroServiceConstants.SolarAmpsServiceConstants.VALIDATE_TOKEN)
    boolean validateToken(@RequestBody Map<String, String> request);
}
