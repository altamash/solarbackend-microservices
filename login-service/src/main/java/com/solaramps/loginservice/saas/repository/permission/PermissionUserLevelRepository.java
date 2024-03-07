package com.solaramps.loginservice.saas.repository.permission;

import com.solaramps.loginservice.saas.model.permission.userLevel.EUserLevel;
import com.solaramps.loginservice.saas.model.permission.userLevel.PermissionUserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionUserLevelRepository extends JpaRepository<PermissionUserLevel, Long> {
    Optional<PermissionUserLevel> findByName(EUserLevel name);
    Set<PermissionUserLevel> findByNameIn(List<EUserLevel> names);
}
