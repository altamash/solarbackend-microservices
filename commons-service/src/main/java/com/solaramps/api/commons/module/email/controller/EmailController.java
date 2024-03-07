package com.solaramps.api.commons.module.email.controller;

import com.sendgrid.Response;
import com.solaramps.api.commons.module.email.dto.EmailDTO;
import com.solaramps.api.commons.module.email.dto.PersonalizationDTO;
import com.solaramps.api.commons.module.email.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
//@CrossOrigin
@RequestMapping("/commons/email")
public class EmailController {

    final EmailService emailService;

    EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    Response sendEmail(@RequestBody EmailDTO emailDTO) throws IOException {
        return emailService.sendEmail(emailDTO);
    }

    @PostMapping("/dynamic")
    Response sendEmailsWithDynamicTemplate(@RequestBody PersonalizationDTO personalizationDTO) throws IOException {
        return emailService.sendEmail(personalizationDTO);
    }
}
