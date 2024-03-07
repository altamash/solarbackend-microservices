package com.solaramps.api.tenant.service;

import com.solaramps.api.tenant.model.User;
import com.solaramps.api.tenant.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class HelperServiceImpl implements HelperService {

    private final UserRepository userRepository;

    public HelperServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserById(Long acctId) {
        return userRepository.findById(acctId).get();
    }
}
