package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.docu.DocuType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocuTypeRepository extends JpaRepository<DocuType, Long> {
    Page<DocuType> findAll(Pageable pageable);
}
