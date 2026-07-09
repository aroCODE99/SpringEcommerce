package com.aro.SpringEcommerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "product_groups")
@Getter @Setter @Builder @NoArgsConstructor
@AllArgsConstructor
public class ProductGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, message = "plz enter valid group name")
    @Column(name = "group_name")
    private String groupName;
    private Instant createdAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<GroupVariant> groupVariants;

    public ProductGroup(String id){
        this.id = Long.parseLong(id);
    }


    public String toString() {
        return getGroupName();
    }

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}
