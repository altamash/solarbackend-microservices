package com.solaramps.api.commons.module.queue.alert.service;

import com.solaramps.api.commons.module.queue.data.WorkflowNotificationLogDTO;

public interface AlertService {

    void handleAlert(WorkflowNotificationLogDTO notification);
}
