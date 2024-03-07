package com.solaramps.loginservice.tenant.repository;

import com.solaramps.loginservice.tenant.model.TenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TenantConfigRepository extends JpaRepository<TenantConfig, Long> {

    Optional<TenantConfig> findByParameter(String parameter);

    List<TenantConfig> findAllByParameterIn(List<String> parameters);
    TenantConfig findFirstByCategory(String category);

    @Query("select tc.text from TenantConfig tc where tc.parameter = :parameter")
    List<String> findAllEmailDomain(@Param("parameter") String parameter);

}
