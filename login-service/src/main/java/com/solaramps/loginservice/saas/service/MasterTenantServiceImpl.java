package com.solaramps.loginservice.saas.service;

import com.solaramps.loginservice.exception.NotFoundException;
import com.solaramps.loginservice.exception.SolarApiException;
import com.solaramps.loginservice.tenant.mapper.responseDTO.userResponse.EUserStatus;
import com.solaramps.loginservice.saas.configuration.DBContextHolder;
import com.solaramps.loginservice.saas.model.tenant.MasterTenant;
import com.solaramps.loginservice.saas.repository.MasterTenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MasterTenantServiceImpl implements MasterTenantService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private MasterTenantRepository masterTenantRepository;


    @Override
    public MasterTenant save(MasterTenant masterTenant) {
        return masterTenantRepository.save(masterTenant);
    }

    @Override
    public MasterTenant update(MasterTenant masterTenant) {
        return masterTenantRepository.save(masterTenant);
    }

    public MasterTenant findById(Long id) {
        LOGGER.info("findByClientId() method call...");
        MasterTenant masterTenant = masterTenantRepository.findByIdFetchTenantRoles(id);
        if (null == masterTenant || masterTenant.getStatus().toUpperCase().equals(EUserStatus.INACTIVE)) {
            throw new SolarApiException("Please contact service provider.");
        }
        return masterTenant;
    }

    @Override
    public MasterTenant findByUserName(String userName) {
        return masterTenantRepository.findByUserName(userName);
    }

    @Override
    public MasterTenant findByUserNameFetchTenantRoles(String userName) {
        return masterTenantRepository.findByUserNameFetchTenantPermissions(userName);
    }

    @Override
    public MasterTenant findByCompanyKey(Long companyKey) {
        return masterTenantRepository.findByCompanyKey(companyKey);
    }

    @Override
    public MasterTenant findByCompanyCode(String companyCode) {
        MasterTenant masterTenant = masterTenantRepository.findByCompanyCode(companyCode);
        if (masterTenant == null) {
            throw new NotFoundException("Company Not Found");
        }
        return masterTenant;
    }

    @Override
    public MasterTenant findByDbName(String dbName) {
        return masterTenantRepository.findByDbName(dbName);
    }

    @Override
    public MasterTenant setCurrentDb(Long id) {
        MasterTenant masterTenant = findById(id);
        DBContextHolder.setTenantName(masterTenant.getDbName());
        return masterTenant;
    }

    @Override
    public List<MasterTenant> findAll() {
        return masterTenantRepository.findAll().stream()
                .filter(tenant -> tenant.getEnabled() && tenant.getValid())
                .collect(Collectors.toList());
    }

    @Override
    public List<MasterTenant> findAllFetchTenantRoles() {
        return masterTenantRepository.findAllFetchTenantRoles();
    }

    @Override
    public void delete(Long tenantClientId) {
        MasterTenant masterTenant =
                masterTenantRepository.findById(tenantClientId).orElseThrow(() -> new NotFoundException(MasterTenant.class, tenantClientId));
        masterTenantRepository.delete(masterTenant);
    }

    @Override
    public void deleteAll() {
        masterTenantRepository.deleteAll();
    }
}
