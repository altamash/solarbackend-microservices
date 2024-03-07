package com.solaramps.api.commons.tenant.repository;

import com.solaramps.api.commons.tenant.model.WorkflowExecProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowExecProcessRepository extends JpaRepository<WorkflowExecProcess, Long> {
}
