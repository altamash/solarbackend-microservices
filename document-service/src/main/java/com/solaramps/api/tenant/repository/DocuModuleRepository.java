package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.docu.DocuModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocuModuleRepository extends JpaRepository<DocuModule, Long> {
}
