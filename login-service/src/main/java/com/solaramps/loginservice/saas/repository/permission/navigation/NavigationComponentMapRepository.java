package com.solaramps.loginservice.saas.repository.permission.navigation;

import com.solaramps.loginservice.saas.model.permission.navigation.NavigationComponentMap;
import com.solaramps.loginservice.saas.model.permission.navigation.NavigationElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NavigationComponentMapRepository extends JpaRepository<NavigationComponentMap, Long> {

    List<NavigationComponentMap> findByIdIn(List<Long> ids);

    @Query("SELECT DISTINCT ncm.navigationElement FROM NavigationComponentMap ncm WHERE ncm.id in :ids")
    List<NavigationElement> findNavigationElementsByIdIn(List<Long> ids);
}
