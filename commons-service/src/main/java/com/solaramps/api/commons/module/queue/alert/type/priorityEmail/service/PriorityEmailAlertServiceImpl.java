package com.solaramps.api.commons.module.queue.alert.type.priorityEmail.service;

import com.solaramps.api.commons.module.email.service.EmailService;
import com.solaramps.api.commons.module.queue.alert.service.AlertService;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import org.springframework.stereotype.Service;

@Service
public class PriorityEmailAlertServiceImpl implements AlertService {

    private final EmailService emailService;

    public PriorityEmailAlertServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void handleAlert(WorkflowNotificationLogDTO notification) {

    }
}
