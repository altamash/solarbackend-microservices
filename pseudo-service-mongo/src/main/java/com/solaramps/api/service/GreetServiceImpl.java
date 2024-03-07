package com.solaramps.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class GreetServiceImpl implements GreetService {
    @Override
    public ObjectNode info() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ObjectNode messageJson = new ObjectMapper().createObjectNode();
        ObjectNode requestMessage = new ObjectMapper().createObjectNode();
        requestMessage.put("Tenant-id", request.getHeader("Tenant-id"));
        requestMessage.put("authorization", request.getHeader("authorization"));
        return messageJson;
    }
}
