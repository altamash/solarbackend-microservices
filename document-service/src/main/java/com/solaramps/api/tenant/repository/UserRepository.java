package com.solaramps.api.tenant.repository;

import com.solaramps.api.tenant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}