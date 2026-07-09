package com.aro.SpringEcommerce;

import com.aro.SpringEcommerce.Entity.*;
import com.aro.SpringEcommerce.Enums.Roles;
import com.aro.SpringEcommerce.Repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class InitApplication implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private VariantRepo variantRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Roles
        Role adminRole = roleRepo.findByName(Roles.ROLE_ADMIN)
                .orElseGet(() ->
                        roleRepo.save(Role.builder()
                                .name(Roles.ROLE_ADMIN)
                                .build()));

        Role userRole = roleRepo.findByName(Roles.ROLE_USER)
                .orElseGet(() ->
                        roleRepo.save(Role.builder()
                                .name(Roles.ROLE_USER)
                                .build()));

        // Users
        AppUser admin = userRepo.findByEmail("aro@gmail.com")
                .orElseGet(() ->
                        userRepo.save(
                                AppUser.builder()
                                        .name("aro@Goat")
                                        .email("aro@gmail.com")
                                        .password(passwordEncoder.encode("test"))
                                        .userRoles(Set.of(adminRole))
                                        .build()
                        ));

        AppUser testUser = userRepo.findByEmail("test@gmail.com")
                .orElseGet(() ->
                        userRepo.save(
                                AppUser.builder()
                                        .name("Test User")
                                        .email("test@gmail.com")
                                        .password(passwordEncoder.encode("test"))
                                        .userRoles(Set.of(userRole))
                                        .build()
                        ));

        // Product groups
        ProductGroup vehicles = groupRepo.findByGroupName("Vehicles")
                .orElseGet(() ->
                        groupRepo.save(
                                ProductGroup.builder()
                                        .groupName("Vehicles")
                                        .build()
                        ));

        ProductGroup clothes = groupRepo.findByGroupName("Clothes")
                .orElseGet(() ->
                        groupRepo.save(
                                ProductGroup.builder()
                                        .groupName("Clothes")
                                        .build()
                        ));

        // Group Variants
        GroupVariant blue = variantRepo.findByVariantName("Blue")
                .orElseGet(() ->
                        variantRepo.save(
                                GroupVariant.builder()
                                        .variantName("Blue")
                                        .group(vehicles)
                                        .build()));

        GroupVariant white = variantRepo.findByVariantName("White")
                .orElseGet(() ->
                        variantRepo.save(
                                GroupVariant.builder()
                                        .variantName("White")
                                        .group(vehicles)
                                        .build()));

        if (productRepo.count() == 0) {

            productRepo.saveAll(List.of(

                    Product.builder()
                            .name("Shirt")
                            .price(new BigDecimal("24"))
                            .description("Cotton Shirt")
                            .group(clothes)
                            .user(admin)
                            .build(),

                    Product.builder()
                            .name("Sweat Shirt")
                            .price(new BigDecimal("60"))
                            .description("Warm Sweat Shirt")
                            .group(clothes)
                            .user(admin)
                            .build(),

                    Product.builder()
                            .name("Flag")
                            .price(new BigDecimal("24"))
                            .description("Vehicle Flag")
                            .group(vehicles)
                            .user(admin)
                            .build(),

                    Product.builder()
                            .name("Golf V")
                            .price(new BigDecimal("20000"))
                            .description("Volkswagen Golf V")
                            .group(vehicles)
                            .user(admin)
                            .build()

            ));
        }
    }
}
