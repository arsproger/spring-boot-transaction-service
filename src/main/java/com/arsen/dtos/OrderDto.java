package com.arsen.dtos;

import com.arsen.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    private LocalDateTime date;
    private OrderStatus status;
}
