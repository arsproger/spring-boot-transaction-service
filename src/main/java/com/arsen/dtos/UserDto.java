package com.arsen.dtos;

import com.arsen.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserDto {
    private String fullName;
    private String email;
    private String password;
    private BigDecimal balance;
    private Role role;
}
