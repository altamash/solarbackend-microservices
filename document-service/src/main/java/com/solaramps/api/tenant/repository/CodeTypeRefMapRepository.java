package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.CodeTypeRefMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeTypeRefMapRepository extends JpaRepository<CodeTypeRefMap, Long> {
    CodeTypeRefMap findByRefCode(String refCode);
}
