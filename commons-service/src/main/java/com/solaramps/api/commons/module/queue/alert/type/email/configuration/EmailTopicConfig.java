package com.solaramps.api.commons.module.queue.alert.type.email.configuration;

import com.solaramps.api.commons.module.queue.alert.service.AlertService;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class EmailTopicConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailTopicConfig.class);

    @Value(value = "${topic.alert-topic.email.name}")
    private String emailTopicName;

    private final AlertService alertService;

    public EmailTopicConfig(@Qualifier("emailAlertServiceImpl") AlertService alertService) {
        this.alertService = alertService;
    }

    @Bean
    public NewTopic emailTopic() {
        return new NewTopic(emailTopicName, 1, (short) 1);
    }

    @KafkaListener(topics = "${topic.alert-topic.email.name}",
                   groupId = "${topic.alert-topic.email.group-id}",
                   containerFactory = "emailAlertKafkaListenerContainerFactory")
    public void listen(WorkflowNotificationLogDTO message) {
//        System.out.println("Received Message in listener: " + message);
        System.out.println(">>>>>>>>>>>>>>> Kafka msg: " + message.toString());
        LOGGER.info(message.toString());
        alertService.handleAlert(message);
    }
}
