package com.solaramps.api.saas.repository;

import com.solaramps.api.saas.model.tenant.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {

    @Query("SELECT m FROM MasterTenant m LEFT JOIN FETCH m.tenantRoles where m.enabled = true and m.valid = true and m.id = :tenantId")
    MasterTenant findByIdFetchTenantRoles(Long tenantId);

    @Query("SELECT m FROM MasterTenant m where m.enabled = true and m.valid = true and m.userName = :userName")
    MasterTenant findByUserName(String userName);

    @Query("SELECT m FROM MasterTenant m " +
            "LEFT JOIN FETCH m.tenantRoles r " +
            "LEFT JOIN FETCH r.permissionSets " +
            "LEFT JOIN FETCH m.permissionSets " +
            "where m.enabled = true and m.valid = true and m.userName = :userName")
    MasterTenant findByUserNameFetchTenantPermissions(String userName);

    @Query("SELECT m FROM MasterTenant m where m.enabled = true and m.valid = true and m.companyKey = :companyKey")
    MasterTenant findByCompanyKey(Long companyKey);

    @Query("SELECT m FROM MasterTenant m where m.enabled = true and m.valid = true and m.companyCode = :companyCode")
    MasterTenant findByCompanyCode(String companyCode);

    @Query("SELECT m FROM MasterTenant m where m.enabled = true and m.valid = true and m.dbName = :dbName")
    MasterTenant findByDbName(String dbName);

    @Query("SELECT m FROM MasterTenant m where m.enabled = true and m.valid = true and m.dbName in :dbNames")
    List<MasterTenant> findByDbNameIn(List<String> dbNames);

    @Query("SELECT m FROM MasterTenant m LEFT JOIN FETCH m.tenantRoles where m.enabled = true and m.valid = true")
    List<MasterTenant> findAllFetchTenantRoles();
}