package com.solaramps.api.commons.saas.repository;

import com.solaramps.api.commons.saas.model.tenant.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {

    @Query("SELECT m FROM MasterTenant m where m.enabled = true and m.valid = true and m.companyKey = :companyKey")
    MasterTenant findByCompanyKey(Long companyKey);
}