package com.arsen.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
    @ManyToMany
    @JoinTable(
            name = "products_users",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;
    @OneToMany(mappedBy = "product")
    private List<Order> orders;
}
