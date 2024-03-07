package com.solaramps.loginservice.saas.service.systemAttribute;

import com.solaramps.loginservice.saas.model.attribute.SystemAttribute;

import java.util.List;

public interface SystemAttributeService {

    SystemAttribute save(SystemAttribute systemAttribute);

    SystemAttribute update(SystemAttribute systemAttribute);

    SystemAttribute findById(Long id);

    SystemAttribute findByAttributeKey(String attributeKey);

    SystemAttribute findByAttribute(String attribute);

    List<SystemAttribute> findByParentAttribute(String attribute);

    List<SystemAttribute> findAll();

    void delete(Long systemAttributeId);

    void deleteAll();
}
