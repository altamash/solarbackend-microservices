package com.solaramps.loginservice.tenant.service;

import com.solaramps.loginservice.tenant.model.user.User;
import com.solaramps.loginservice.tenant.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByUserNameFetchRoles(String userName) {
        return userRepository.findByUserNameFetchRoles(userName);
    }

}