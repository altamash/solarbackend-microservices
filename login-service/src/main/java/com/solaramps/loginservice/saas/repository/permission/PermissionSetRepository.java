package com.solaramps.loginservice.saas.repository.permission;

import com.solaramps.loginservice.saas.model.permission.PermissionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PermissionSetRepository extends JpaRepository<PermissionSet, Long>
{
   /* @Query("select DISTINCT p from PermissionSet p LEFT JOIN FETCH p.componentLibraries where p.name in :names")
    List<PermissionSet> getPermissionSetsFetchComponentLibrary(Set<String> names);*/

    PermissionSet findByName(String name);

    @Query("SELECT p FROM PermissionSet p LEFT JOIN FETCH p.permissions where p.id in :ids")
    List<PermissionSet> findByIdIn(Set<Long> ids);

    @Query("SELECT p FROM PermissionSet p LEFT JOIN FETCH p.permissions LEFT JOIN FETCH p.userLevels where p.id = :id")
    PermissionSet findByIdFetchPermissions(Long id);

    @Query("SELECT p FROM PermissionSet p LEFT JOIN FETCH p.permissions LEFT JOIN FETCH p.userLevels")
    List<PermissionSet> findAllFetchPermissions();
}
