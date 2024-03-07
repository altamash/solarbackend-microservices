package com.solaramps.api.commons.tenant.repository;

import com.solaramps.api.commons.tenant.model.WorkflowNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkflowNotificationLogRepository extends JpaRepository<WorkflowNotificationLog, Long> {

    List<WorkflowNotificationLog> findByCommTypeAndStatus(String commType, String status);
}
