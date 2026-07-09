package com.aro.SpringEcommerce.Entity;

import com.aro.SpringEcommerce.Enums.Roles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private Roles name;
}

