package com.aro.SpringEcommerce.Repos;

import com.aro.SpringEcommerce.Entity.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepo extends JpaRepository<ProductGroup, Long> {

    Optional<ProductGroup> findByGroupName(String name);
}
