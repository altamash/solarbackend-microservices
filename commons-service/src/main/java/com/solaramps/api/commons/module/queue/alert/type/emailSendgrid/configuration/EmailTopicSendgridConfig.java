package com.solaramps.api.commons.module.queue.alert.type.emailSendgrid.configuration;

import com.solaramps.api.commons.module.queue.alert.service.AlertService;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class EmailTopicSendgridConfig {

    @Value(value = "${topic.alert-topic.email-sendgrid.name}")
    private String emailTopicName;

    private final AlertService alertService;

    public EmailTopicSendgridConfig(@Qualifier("emailAlertServiceSendgridImpl") AlertService alertService) {
        this.alertService = alertService;
    }

    @Bean
    public NewTopic emailSendgridTopic() {
        return new NewTopic(emailTopicName, 1, (short) 1);
    }

    @KafkaListener(topics = "${topic.alert-topic.email-sendgrid.name}",
                   groupId = "${topic.alert-topic.email-sendgrid.group-id}",
                   containerFactory = "emailAlertSendgridKafkaListenerContainerFactory")
    public void listen(WorkflowNotificationLogDTO message) {
        System.out.println("Received Message in listener: " + message);
//        alertService.handleAlert(message);
    }
}
