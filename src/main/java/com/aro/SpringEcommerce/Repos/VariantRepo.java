package com.aro.SpringEcommerce.Repos;

import com.aro.SpringEcommerce.Entity.GroupVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantRepo extends JpaRepository<GroupVariant, Long> {

    Optional<GroupVariant> findByVariantName(String name);
}
