package com.solaramps.api.commons.module.queue.alert.service;

import com.solaramps.api.commons.module.queue.data.Record;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import com.solaramps.api.commons.module.storage.dto.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface QueuesService {
    void sendToTopic(String producer, String topic, String message);

    void sendMessage(String topic, WorkflowNotificationLogDTO notificationLog) throws ExecutionException, InterruptedException;

    BaseResponse sendMessage(Long compKey,
                             Long tenantConfigId,
                             String subject,
                             List<String> tos,
                             List<String> ccs,
                             List<String> bccs,
                             String placeholderValuesJson,
                             List<MultipartFile> files,
                             List<String> fileNames,
                             String brandId);

    List<Record> runConsumer(String topic);
}
