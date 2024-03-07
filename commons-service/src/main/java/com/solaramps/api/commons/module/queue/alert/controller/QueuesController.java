package com.solaramps.api.commons.module.queue.alert.controller;

import com.solaramps.api.commons.module.queue.alert.service.QueuesService;
import com.solaramps.api.commons.module.queue.data.Record;
import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;
import com.solaramps.api.commons.module.storage.dto.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
//@CrossOrigin
@RequestMapping("/queue")
public class QueuesController {

    final QueuesService queuesService;

    QueuesController(QueuesService queuesService) {
        this.queuesService = queuesService;
    }

    @PostMapping(value = "/message/topic/{topic}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void sendToTopic(@RequestBody WorkflowNotificationLogDTO notificationLog, @PathVariable String topic) throws ExecutionException, InterruptedException {
        queuesService.sendMessage(topic, notificationLog);
    }

    @PostMapping(value = "/notification", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse trackEvent(
//            @RequestParam String distinctId,
//                      @RequestParam String eventName,
            @RequestHeader("Comp-Key") Long compKey,
            @RequestParam Long tenantConfigId,
            @RequestParam(required = false) String subject,
            @RequestParam List<String> tos,
            @RequestParam(required = false) List<String> ccs,
            @RequestParam(required = false) List<String> bccs,
            @RequestPart(required = false) String placeholderValuesJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(required = false) List<String> fileNames,
            @RequestParam(required = false) String brandId) {
//        return queuesService.sendMessage(tenantConfigId, subject, tos, ccs, bccs, file, fileName);
        return queuesService.sendMessage(compKey, tenantConfigId, subject, tos, ccs, bccs, placeholderValuesJson, files, fileNames, brandId);
//        queuesService.sendMessage(null, null, null, null, null, properties, file, fileName);
    }

    @GetMapping("/message/topic/{topic}")
    public List<Record> runConsumer(@PathVariable String topic) {
        return queuesService.runConsumer(topic);
    }
}
