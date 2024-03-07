package com.solaramps.api.commons.module.queue.alert.type.priorityEmail.configuration;

import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class PriorityEmailTopicConfig {

    @Value(value = "${topic.alert-topic.priority-email.name}")
    private String priorityEmailTopicName;

    @Bean
    public NewTopic priorityEmailTopic() {
        return new NewTopic(priorityEmailTopicName, 1, (short) 1);
    }

    @KafkaListener(topics = "${topic.alert-topic.priority-email.name}",
                   groupId = "${topic.alert-topic.priority-email.group-id}",
                   containerFactory = "priorityEmailAlertKafkaListenerContainerFactory")
    public void listen(WorkflowNotificationLogDTO message) {
        System.out.println("Received Message in listener: " + message);
    }
}
