package com.solaramps.api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solaramps.api.service.GreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/greet")
public class GreetController {
    @Autowired
    private GreetService greetService;

    @GetMapping("/info")
    public ResponseEntity<ObjectNode> info() {
            return new ResponseEntity<>(greetService.info(), HttpStatus.OK);
    }

}
