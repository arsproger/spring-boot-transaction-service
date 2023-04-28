package com.arsen.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal balance;
    @OneToMany(mappedBy = "company")
    private List<Product> products;
}
