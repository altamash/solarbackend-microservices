package com.solaramps.api.tenant.service;

import com.solaramps.api.tenant.model.User;

public interface HelperService {

    User findUserById(Long acctId);
}
