package com.solaramps.loginservice.tenant.service;

import com.solaramps.loginservice.tenant.model.user.User;

public interface UserService {

    User findByUserNameFetchRoles(String userName);

}
