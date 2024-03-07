package com.solaramps.loginservice.saas.service;

import com.solaramps.loginservice.saas.model.tenant.MasterTenant;

import java.util.List;

public interface MasterTenantService {

    MasterTenant save(MasterTenant masterTenant);

    MasterTenant update(MasterTenant masterTenant);

    MasterTenant findById(Long id);

    MasterTenant findByUserName(String userName);

    MasterTenant findByUserNameFetchTenantRoles(String userName);

    MasterTenant findByCompanyKey(Long companyKey);

    MasterTenant findByCompanyCode(String companyCode);

    MasterTenant findByDbName(String dbName);

    MasterTenant setCurrentDb(Long id);

    List<MasterTenant> findAll();

    List<MasterTenant> findAllFetchTenantRoles();

    void delete(Long tenantClientId);

    void deleteAll();
}