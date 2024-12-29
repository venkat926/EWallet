package org.kvn.UserService.repository;

import org.kvn.UserService.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepo extends JpaRepository<Users, Integer> {
    UserDetails findByEmail(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNo(String phoneNo);

    boolean existsByUserIdentifierValue(String userIdentifierValue);
}
