package com.solaramps.loginservice.saas.repository.permission;

import com.solaramps.loginservice.saas.model.permission.component.ComponentLibrary;
import com.solaramps.loginservice.saas.model.permission.component.ComponentTypeProvision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComponentLibraryRepository extends JpaRepository<ComponentLibrary, Long> {

    List<ComponentLibrary> findByLevel(Integer level);

    ComponentLibrary findByComponentName(String componentName);

    List<ComponentLibrary> findByParentId(Long parentId);

    List<ComponentLibrary> findByComponentTypeProvision(ComponentTypeProvision componentTypeProvision);

    @Query("SELECT MAX(c.id) FROM ComponentLibrary c WHERE c.componentTypeProvision.id = :componentTypeProvisionId")
    Long getLastIdentifier(Long componentTypeProvisionId);

    @Query("SELECT MAX(c.id) FROM ComponentLibrary c")
    Long getLastIdentifier();
}
