package com.solaramps.api.tenant.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("DefaultController")
@RequestMapping(value = "/")
public class DefaultController {

    @GetMapping("/ping")
    public ObjectNode ping(@RequestParam(required = false) String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode messageJson = mapper.createObjectNode();
        messageJson.put("message", token);
        messageJson.put("code", 200);
        return messageJson;
    }
}
