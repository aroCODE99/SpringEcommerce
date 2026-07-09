package com.aro.SpringEcommerce.Entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Entity
@Table(name = "group_variants")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class GroupVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "variant_name")
    private String variantName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonBackReference
    @JsonIgnore
    private ProductGroup group;

    public String toString() {
        return getVariantName();
    }
}
