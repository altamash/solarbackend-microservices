package com.solaramps.api.commons.module.queue.alert.type.email.service;

import com.sendgrid.Response;
import com.solaramps.api.commons.module.email.dto.EmailDTO;
import com.solaramps.api.commons.module.email.dto.PersonalizationDTO;
import com.solaramps.api.commons.module.email.service.EmailService;
import com.solaramps.api.commons.module.notification.email.suprSend.service.SuprSendService;
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
public class EmailAlertServiceImpl implements AlertService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final WorkflowNotificationLogRepository workflowNotificationLogRepository;
    private final WorkflowExecProcessRepository workflowExecProcessRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SuprSendService suprSendService;

    public EmailAlertServiceImpl(WorkflowNotificationLogRepository workflowNotificationLogRepository, WorkflowExecProcessRepository workflowExecProcessRepository, UserRepository userRepository, EmailService emailService, SuprSendService suprSendService) {
        this.workflowNotificationLogRepository = workflowNotificationLogRepository;
        this.workflowExecProcessRepository = workflowExecProcessRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.suprSendService = suprSendService;
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
            /*if (notification.getTos() == null) {
                notification.setTos(new ArrayList<>());
            }
            notification.getCcs().add(notification.getDestInfo());
            if (notification.getCcs() == null) {
                notification.setCcs(new ArrayList<>());
            }
            if (notification.getBccs() == null) {
                notification.setBccs(new ArrayList<>());
            }*/
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

            Map<String, Object> properties = new HashMap<>();
//            notification.getPlaceholderValues().entrySet().forEach(entry -> {
//                properties.put(entry.getKey(), entry.getValue());
//            });
            result = suprSendService.trackEvent(personalizationDTO.getTos().get(0).getEmail(),
                    notification.getEventName(), notification.getPropertiesJsonString(), notification.getFileAttachments(), notification.getBrandId());
            LOGGER.info(">>>>>>>>>>> personalizationDTO.getTos().get(0).getEmail() " + personalizationDTO.getTos().get(0).getEmail());
            LOGGER.info(">>>>>>>>>>> notification.getEventName() " + notification.getEventName());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            if (response == null) {
                return new EmailResponse(EWorkflowNotificationLog.NOT_SENT.name(), null);
            }
        }
        return new EmailResponse(EWorkflowNotificationLog.SENT.name(), result);
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
