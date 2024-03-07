package com.solaramps.cloud.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FallBackMethodController {

    @Autowired
    ObjectMapper mapper;

    @GetMapping("/authServiceFallBack")
    public ObjectNode uauthServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Authentication application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/loginServiceFallBack")
    public ObjectNode loginServiceFallBack() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Login application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/userServiceFallBackMethod")
    public ObjectNode userServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "User application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/userDetailServiceFallBackMethod")
    public ObjectNode userDetailServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "User detail application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/pseudoServiceFallBackMethod")
    public ObjectNode pseudoServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Pseudo application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/pseudoServiceMongoFallBackMethod")
    public ObjectNode pseudoServiceMongoFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Pseudo Mongo application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/siForecastFallBackMethod")
    public ObjectNode siForecastFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "SI Forecast application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/documentServiceFallBackMethod")
    public ObjectNode documentServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Document Management application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/investorServiceFallBackMethod")
    public ObjectNode investorServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Investor application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }
    @PostMapping("/documentServiceFallBackMethod")
    public ObjectNode documentServiceFallBackPostMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Document Management application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @PutMapping("/documentServiceFallBackMethod")
    public ObjectNode documentServiceFallBackPutMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Document Management application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @DeleteMapping("/documentServiceFallBackMethod")
    public ObjectNode documentServiceFallBackDeleteMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Document Management application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @PostMapping("/mpofferServiceFallBackMethod")
    public ObjectNode mpofferServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "MP Offer application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/commonsServiceFallBackMethod")
    public ObjectNode commonsServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "Commons application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

    @GetMapping("/projectmanagementServiceFallBackMethod")
    public ObjectNode projectmanagementServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "ProjectManagement application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }


    @GetMapping("/chatmanagementServiceFallBackMethod")
    public ObjectNode chatmanagementServiceFallBackMethod() {
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", "ChatManagement application is closed for maintenance.");
        messageJson.put("code", 503);
        return messageJson;
    }

}
