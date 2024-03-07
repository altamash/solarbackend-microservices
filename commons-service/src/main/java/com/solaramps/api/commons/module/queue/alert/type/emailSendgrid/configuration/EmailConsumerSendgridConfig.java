package com.solaramps.api.commons.module.queue.alert.type.emailSendgrid.configuration;

import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EmailConsumerSendgridConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${topic.alert-topic.email-sendgrid.group-id}")
    private String emailAlertGroupId;

    public ConsumerFactory<String, WorkflowNotificationLogDTO> emailAlertConsumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(WorkflowNotificationLogDTO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorkflowNotificationLogDTO> emailAlertSendgridKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WorkflowNotificationLogDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setConsumerFactory(emailAlertConsumerFactory(emailAlertGroupId));
        return factory;
    }
}
