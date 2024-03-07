package com.solaramps.loginservice.tenant.repository;

import com.solaramps.loginservice.tenant.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles where u.userName = :userName")
    User findByUserNameFetchRoles(String userName);
}
