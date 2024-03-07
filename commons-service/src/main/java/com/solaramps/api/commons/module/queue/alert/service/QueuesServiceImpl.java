package com.solaramps.api.commons.module.queue.alert.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solaramps.api.commons.Utility;
import com.solaramps.api.commons.module.notification.email.suprSend.service.SuprSendService;
import com.solaramps.api.commons.module.queue.alert.AlertUtility;
import com.solaramps.api.commons.module.queue.alert.mapper.FileAttachment;
import com.solaramps.api.commons.module.queue.alert.mapper.TenantConfigTextJson;
import com.solaramps.api.commons.module.queue.data.Record;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import com.solaramps.api.commons.module.storage.dto.BaseResponse;
import com.solaramps.api.commons.saas.repository.MasterTenantRepository;
import com.solaramps.api.commons.tenant.model.TenantConfig;
import com.solaramps.api.commons.tenant.repository.TenantConfigRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class QueuesServiceImpl implements QueuesService {

    public static Logger LOGGER = LoggerFactory.getLogger(QueuesServiceImpl.class.getName());
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /*@Autowired
    private KafkaTemplate<String, WorkflowNotificationLogDTO> emailAlertKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, WorkflowNotificationLogDTO> priorityEmailAlertKafkaTemplate;*/

    @Autowired
    private KafkaTemplate<String, WorkflowNotificationLogDTO> alertKafkaTemplate;

    private final MasterTenantRepository masterTenantRepository;
    private final TenantConfigRepository tenantConfigRepository;
//    private final StorageService storageService;
    private final SuprSendService suprSendService;
//    private final String sasToken;
//    private final String blobService;
//    private final String container;
//    private SimpleDateFormat dateFormat = new SimpleDateFormat(Utility.SYSTEM_DATE_FORMAT);
    private final AlertUtility alertUtility;

    public QueuesServiceImpl(MasterTenantRepository masterTenantRepository, TenantConfigRepository tenantConfigRepository,
//                             StorageService storageService,
                             SuprSendService suprSendService,
//                             @Value("${app.storage.azureBlobSasToken}") String sasToken,
//                             @Value("${app.storage.blobService}") String blobService,
//                             @Value("${app.storage.container}") String container,
                             AlertUtility alertUtility) {
        this.masterTenantRepository = masterTenantRepository;
        this.tenantConfigRepository = tenantConfigRepository;
//        this.storageService = storageService;
        this.suprSendService = suprSendService;
//        this.sasToken = sasToken;
//        this.blobService = blobService;
//        this.container = container;
        this.alertUtility = alertUtility;
    }

    @Override
    public void sendToTopic(String producer, String topic, String message) {
        /*KafkaProducer<Long, String> firstProducer = null;
        try {
            // Creating producer
            firstProducer = (KafkaProducer<Long, String>) createProducer(producer + System.currentTimeMillis());
            // Create producer record
            ProducerRecord<Long, String> record = new ProducerRecord<>(topic, message);
            // Sending the data
            Future<RecordMetadata> metadata = firstProducer.send(record, (recordMetadata, e) -> {
                if (e == null) {
                    logger.info("Successfully received the details as: \n" +
                            "Topic: " + recordMetadata.topic() + "\n" +
                            "Partition: " + recordMetadata.partition() + "\n" +
                            "Offset: " + recordMetadata.offset() + "\n" +
                            "Timestamp: " + recordMetadata.timestamp());
                } else {
                    logger.error("Can't produce,getting error", e);

                }
            });
            firstProducer.flush();
            firstProducer.close();
//            metadata.isDone();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            *//*if (firstProducer != null) {
                firstProducer.flush();
                firstProducer.close();
            }*//*
        }*/
        // Creating producer
        KafkaProducer<String, String> firstProducer = (KafkaProducer<String, String>) createProducer(producer);
        // Create producer record
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        // Sending the data
        Future<RecordMetadata> metadata = firstProducer.send(record, (recordMetadata, e) -> {
            if (e == null) {
                LOGGER.info("Successfully received the details as: \n" +
                        "Topic: " + recordMetadata.topic() + "\n" +
                        "Partition: " + recordMetadata.partition() + "\n" +
                        "Offset: " + recordMetadata.offset() + "\n" +
                        "Timestamp: " + recordMetadata.timestamp());
            } else {
                LOGGER.error("Can't produce,getting error", e);

            }
        });
        firstProducer.flush();
        firstProducer.close();
//        runConsumer(topic);
    }

    @Override
    public void sendMessage(String topic, WorkflowNotificationLogDTO notificationLog) throws ExecutionException, InterruptedException {
        CompletableFuture<SendResult<String, WorkflowNotificationLogDTO>> future = null;
        try {
            alertKafkaTemplate.send(topic, notificationLog);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse sendMessage(Long compKey, Long tenantConfigId, String subject, List<String> tos,
                                    List<String> ccs, List<String> bccs, String placeholderValuesJson,
                                    List<MultipartFile> files, List<String> fileNames, String brandId) {
//        suprSendService.addSubscriberAndEmail(tos.get(0), List.of(tos.get(0)));
        tos.forEach(to -> suprSendService.addSubscriberAndEmail(to, List.of(to)));
        WorkflowNotificationLogDTO notificationLog;
        TenantConfig config = tenantConfigRepository.findById(tenantConfigId).orElse(null);
        if (config == null) {
            return BaseResponse.builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message("Tenant config with id " + tenantConfigId + " not found")
                    .build();
        }
        TenantConfigTextJson textJson;
        try {
            ObjectMapper mapper = new ObjectMapper();
            textJson = mapper.readValue(config.getText(), TenantConfigTextJson.class);

//            fileName = updateFileName(file, fileName);
            List<FileAttachment> fileAttachments = new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                for (int i = 0, filesSize = files.size(); i < filesSize; i++) {
                    MultipartFile file = files.get(i);
                    String fileName = fileNames != null && fileNames.size() == files.size() ? fileNames.get(i) : null;
                    fileName = alertUtility.updateFileName(file, fileName);
                    String path = alertUtility.getFilePath(file, fileName, textJson.getAttachmentDirRef());
                    fileAttachments.add(FileAttachment.builder()
                                    .name(fileName)
                                    .path(path)
                            .build());
                }
            }

            notificationLog = WorkflowNotificationLogDTO.builder()
                    .eventName(textJson.getEventName())
                    .commType(config.getParameter())
                    .toCSV(tos.stream().collect(Collectors.joining(",")))
                    .ccCSV(ccs != null ? ccs.stream().collect(Collectors.joining(",")) : null)
                    .bccCSV(bccs != null ? bccs.stream().collect(Collectors.joining(",")) : null)
                    .toCSV(tos.stream().collect(Collectors.joining(",")))
                    .placeholderValues(null)
                    .propertiesJsonString(updateProperties(textJson, mapper, subject, tos, ccs, bccs, placeholderValuesJson))
                    .tenantName(masterTenantRepository.findByCompanyKey(compKey).getDbName())
//                    .fileAttachmentPath(getFilePath(file, fileName, textJson.getAttachmentDirRef()))
//                    .fileAttachmentPath(getFilePath(null, fileNames.get(0), textJson.getAttachmentDirRef()))
//                    .fileAttachmentName(fileNames.get(0))
                    .fileAttachments(fileAttachments)
                    .brandId(brandId)
                    .build();
        } catch (JsonMappingException e) {
            return BaseResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
        } catch (JsonProcessingException e) {
            return BaseResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
        }
        try {
            LOGGER.info(">>>>>>>>>> textJson.getTopic(): " + textJson.getTopic());
            LOGGER.info(notificationLog.toString());
            sendMessage(textJson.getTopic(), notificationLog);
            return BaseResponse.builder()
                    .code(HttpStatus.OK.value())
                    .message("SUCCESS")
                    .build();
        } catch (ExecutionException e) {
            return BaseResponse.builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
        } catch (InterruptedException e) {
            return BaseResponse.builder()
                    .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .message(e.getMessage())
                    .build();
        }
    }

    private String updateProperties(TenantConfigTextJson textJson, ObjectMapper mapper,
                                 String subject, List<String> tos, List<String> ccs, List<String> bccs,
                                 String placeholderValuesJson) throws JsonProcessingException {
//        Map<String, Object> properties = new HashMap<>();
        ObjectNode node = mapper.createObjectNode();
        node.put(textJson.getSubjectName(), subject != null ? subject : textJson.getSubjectDefault());
//        tos.remove(0);
        node.put(textJson.getToListName(), tos.stream().collect(Collectors.joining(","))); // TODO: CHECK
        if (ccs != null && !ccs.isEmpty()) {
            node.put(textJson.getCcListName(), ccs.stream().collect(Collectors.joining(",")));
        }
        if (bccs != null && !bccs.isEmpty()) {
            node.put(textJson.getBccListName(), bccs.stream().collect(Collectors.joining(",")));
        }
//        properties.put("recipient", "");
//        properties.put("sender", "Solar Admin");
        ObjectNode json = mapper.createObjectNode();
        json.setAll(node);
//        properties.keySet().forEach(key -> json.put(key, properties.get(key)));
//        node.keySet().forEach(key -> json.put(key, properties.get(key)));

        if (placeholderValuesJson != null) {
            try {
//                JSONObject placeholderJson = new JSONObject(placeholderValuesJson);

                json.setAll((ObjectNode) mapper.readTree(placeholderValuesJson));

//                properties.keySet().forEach(key -> json.put(key, json.get(key)));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        /*properties.put("alert_details", new JSONArray().put(new JSONObject()
                        .put("date", "Monday Aug 19 2023")
                        .put("time", "3:00 PM - 5:00 PM")
                        .put("weather", "Rainy")
                        .put("production", "20% - 40%")
                )
                .put(new JSONObject()
                        .put("date", "Monday Aug 19 2023")
                        .put("time", "3:00 PM - 5:00 PM")
                        .put("weather", "Rainy")
                        .put("production", "20% - 40%")
                ));*/
/*
        Iterator<String> keysIterator = placeholderJson.keys();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            properties.put(key, properties.put(key, placeholderJson.get(key)));
        }
*/
        return mapper.writeValueAsString(json);
    }

    private Producer<String, String> createProducer(String producerName) {
        java.util.Properties props = new java.util.Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producerName);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    @Override
    public List<Record> runConsumer(String topic) {
        long readStart = System.currentTimeMillis();
        final Consumer<Long, String> consumer = createConsumer(topic);
        final int giveUp = 10;   int noRecordsCount = 0;
        List<Record> recordList = new ArrayList<>();
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(100);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) {
                    break;
                }
                else {
                    continue;
                }
            }
            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
                recordList.add(Record.builder().key(record.key()).value(record.value())
                        .partition(record.partition()).offset(record.offset()).build());
            });

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE reading records in " + Utility.getFormattedMillis(System.currentTimeMillis() - readStart));
        return recordList;
    }

    private Consumer<Long, String> createConsumer(String topic) {
        final java.util.Properties props = new java.util.Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "Group_Id_" + System.currentTimeMillis());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create the consumer using props.
        final Consumer<Long, String> consumer =
                new KafkaConsumer<>(props);

        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }
}
