package com.solaramps.loginservice.saas.repository.permission;

import com.solaramps.loginservice.saas.model.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
   /* @Query("select DISTINCT p from PermissionSet p LEFT JOIN FETCH p.componentLibraries where p.name in :names")
    List<PermissionSet> getPermissionSetsFetchComponentLibrary(Set<String> names);*/

    Permission findByName(String name);

    List<Permission> findByIdIn(Set<Long> ids);

    @Query("SELECT MAX(p.id) FROM Permission p WHERE p.componentLibrary.componentTypeProvision.id = :componentTypeProvisionId")
    Long getLastIdentifier(Long componentTypeProvisionId);

    @Query("SELECT MAX(p.id) FROM Permission p")
    Long getLastIdentifier();

    List<Permission> findByNameIn(List<String> names);
}
