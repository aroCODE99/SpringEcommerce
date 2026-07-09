package com.aro.SpringEcommerce.Repos;

import com.aro.SpringEcommerce.Entity.Role;
import com.aro.SpringEcommerce.Enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Roles name);
}
