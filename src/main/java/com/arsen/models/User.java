package com.arsen.models;

import com.arsen.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private BigDecimal balance;
    private BigDecimal reserveBalance;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToMany(mappedBy = "users")
    private List<Product> products;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
