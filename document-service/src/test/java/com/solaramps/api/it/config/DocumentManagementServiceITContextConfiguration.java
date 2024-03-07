package com.solaramps.api.it.config;

import com.solaramps.api.it.config.saas.MasterDatabaseConfigIT;
import com.solaramps.api.it.config.saas.MasterDatabaseConfigPropertiesIT;
import com.solaramps.api.it.config.tenant.DataSourceBasedMultiTenantConnectionProviderImplIT;
import com.solaramps.api.it.config.tenant.TenantDatabaseConfigIT;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DocumentManagementServiceITContextConfiguration {

    // SAAS configuration components
    @Bean
    public MasterDatabaseConfigIT masterDatabaseConfig() {
        return new MasterDatabaseConfigIT();
    }

    @Bean
    public MasterDatabaseConfigPropertiesIT masterDatabaseConfigProperties() {
        return new MasterDatabaseConfigPropertiesIT();
    }

    // Tenant configuration components
    @Bean
    public DataSourceBasedMultiTenantConnectionProviderImplIT dataSourceBasedMultiTenantConnectionProviderImpl() {
        return new DataSourceBasedMultiTenantConnectionProviderImplIT();
    }

    @Bean
    public TenantDatabaseConfigIT tenantDatabaseConfig() {
        return new TenantDatabaseConfigIT();
    }
}
