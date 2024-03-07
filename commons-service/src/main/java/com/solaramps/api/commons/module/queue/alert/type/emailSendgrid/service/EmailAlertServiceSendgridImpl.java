package com.solaramps.api.commons.module.queue.alert.type.emailSendgrid.service;

import com.sendgrid.Response;
import com.solaramps.api.commons.module.email.dto.EmailDTO;
import com.solaramps.api.commons.module.email.dto.PersonalizationDTO;
import com.solaramps.api.commons.module.email.service.EmailService;
import com.solaramps.api.commons.module.queue.alert.constant.EWorkflowDestType;
import com.solaramps.api.commons.module.queue.alert.constant.EWorkflowNotificationLog;
import com.solaramps.api.commons.module.queue.alert.service.AlertService;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import com.solaramps.api.commons.saas.configuration.DBContextHolder;
import com.solaramps.api.commons.tenant.model.User;
import com.solaramps.api.commons.tenant.model.WorkflowExecProcess;
import com.solaramps.api.commons.tenant.model.WorkflowNotificationLog;
import com.solaramps.api.commons.tenant.repository.UserRepository;
import com.solaramps.api.commons.tenant.repository.WorkflowExecProcessRepository;
import com.solaramps.api.commons.tenant.repository.WorkflowNotificationLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailAlertServiceSendgridImpl implements AlertService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final WorkflowNotificationLogRepository workflowNotificationLogRepository;
    private final WorkflowExecProcessRepository workflowExecProcessRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public EmailAlertServiceSendgridImpl(WorkflowNotificationLogRepository workflowNotificationLogRepository,
                                         WorkflowExecProcessRepository workflowExecProcessRepository,
                                         UserRepository userRepository, EmailService emailService) {
        this.workflowNotificationLogRepository = workflowNotificationLogRepository;
        this.workflowExecProcessRepository = workflowExecProcessRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Async
    @Override
    public void handleAlert(WorkflowNotificationLogDTO notification) {
        try {
            DBContextHolder.setTenantName(notification.getTenantName());
            EmailResponse emailResponse = sendNotification(notification);
            if (notification.getTenantName() != null) {
                persistNotification(notification, emailResponse);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private EmailResponse sendNotification(WorkflowNotificationLogDTO notification) {
        Response response = null;
        String result = null;
        try {
            PersonalizationDTO personalizationDTO = PersonalizationDTO.builder()
                    .tos(notification.getDestInfo() != null ? List.of(EmailDTO.builder().email(notification.getDestInfo()).build()) :
                            List.of(notification.getToCSV().split(",")).stream()
                                    .map(String::trim)
                                    .map(email -> EmailDTO.builder()
                                            .email(email)
                                            .build())
                                    .collect(Collectors.toList()))
                    .ccs(notification.getCcCSV() != null ? List.of(notification.getCcCSV().split(",")).stream()
                            .map(String::trim)
                            .map(email -> EmailDTO.builder()
                                    .email(email)
                                    .build())
                            .collect(Collectors.toList()) : null)
                    .bccs(notification.getBccCSV() != null ? List.of(notification.getBccCSV().split(",")).stream()
                            .map(String::trim)
                            .map(email -> EmailDTO.builder()
                                    .email(email)
                                    .build())
                            .collect(Collectors.toList()) : null)
                    .build();
            personalizationDTO.setTemplateId(notification.getTemplateId());
            setMessage(notification.getPlaceholderValues(), notification.getRefCode(), notification.getRefId());
            notification.getPlaceholderValues().entrySet().forEach(entry -> personalizationDTO.addDynamicTemplateData(entry.getKey(), entry.getValue()));
            response = emailService.sendEmail(personalizationDTO);

            Map<String, String> properties = new HashMap<>();
            notification.getPlaceholderValues().entrySet().forEach(entry -> {
                properties.put(entry.getKey(), (String) entry.getValue());
            });
            response = emailService.sendEmail(personalizationDTO);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (response == null) {
                return new EmailResponse(EWorkflowNotificationLog.NOT_SENT.name(), null);
            }
        }
        String message = response.getBody();
        Integer maxSize = 255;
        if(message.length() > maxSize ){
            message = message.substring(0, maxSize);
        }
        if (response.getStatusCode() != 200 && response.getStatusCode() != 202) {
            return new EmailResponse(EWorkflowNotificationLog.NOT_SENT.name(), message);
        }
        return new EmailResponse(EWorkflowNotificationLog.SENT.name(), message);
    }

    private void setMessage(Map<String, Object> placeholderValues, String refCode, Long refId) {
        String message = (String) placeholderValues.get("message");
        if (message == null || message.isBlank()) {
            if ("WorkflowNotificationLog".equals(refCode) && refCode != null && refId != null) {
                Optional<WorkflowNotificationLog> logOptional = workflowNotificationLogRepository.findById(refId);
                if (logOptional.isPresent()) {
                    placeholderValues.put("message", logOptional.get().getMessage());
                }
            }
        }
    }

    private void persistNotification(WorkflowNotificationLogDTO notification, EmailResponse emailResponse) {
        WorkflowNotificationLog workflowNotificationLog = null;
        if (notification.getId() == null) {
            Optional<WorkflowExecProcess> workflowExecProcess = Optional.empty();
            if (notification.getWorkflowExecProcessId() != null) {
                workflowExecProcess = workflowExecProcessRepository.findById(notification.getWorkflowExecProcessId());
            }
            User recipient = null;
            if (notification.getRecipientId() != null) {
                Optional<User> recipientOptional = userRepository.findById(notification.getRecipientId());
                if (recipientOptional.isPresent()) {
                    recipient = recipientOptional.get();
                }
            }
            workflowNotificationLog = WorkflowNotificationLog.builder()
                    .workflowExecProcess(workflowExecProcess.isPresent() ? workflowExecProcess.get() : null)
                    .recipient(recipient)
                    .destInfo(notification.getDestInfo())
                    .destType(EWorkflowDestType.EMAIL.name())
                    .commType("e")
//                        .message(getMessage(templateHTMLCode, placeholderValues, recipient))
                    .toCSV(notification.getToCSV())
                    .ccCSV(notification.getCcCSV())
                    .bccCSV(notification.getBccCSV())
                    .status(emailResponse.status())
                    .errorLog(emailResponse.message())
                    .build();
        } else {
            Optional<WorkflowNotificationLog> logOptional = workflowNotificationLogRepository.findById(notification.getId());
            if (logOptional.isPresent()) {
                workflowNotificationLog = logOptional.get();
                workflowNotificationLog.setStatus(emailResponse.status());
                workflowNotificationLog.setErrorLog(emailResponse.message());
            }
        }
        if (workflowNotificationLog != null) {
            workflowNotificationLogRepository.save(workflowNotificationLog);
        }
    }

    record EmailResponse (String status, String message) {}
}
