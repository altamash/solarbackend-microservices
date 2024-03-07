package com.solaramps.loginservice.saas.repository.permission.navigation;

import com.solaramps.loginservice.saas.model.permission.navigation.NavigationElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NavigationElementRepository extends JpaRepository<NavigationElement, Long> {

    List<NavigationElement> findByParent(Long id);

//    List<NavigationElement> findByParentIn(List<Long> ids);
}
