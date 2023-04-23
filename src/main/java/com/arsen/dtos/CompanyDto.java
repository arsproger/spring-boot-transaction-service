package com.arsen.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CompanyDto {
    private String name;
    private BigDecimal balance;
}
