package com.solaramps.api.commons.tenant.repository;

import com.solaramps.api.commons.tenant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
