package com.arsen.controllers;

import com.arsen.dtos.OrderDto;
import com.arsen.mappers.OrderMapper;
import com.arsen.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderMapper.toDto(orderService.getOrderById(id));
    }

    @PostMapping
    public Long createOrder(@RequestBody OrderDto orderDto) {
        return orderService.createOrder(orderMapper.toEntity(orderDto));
    }

    @PutMapping("/{id}")
    public Long updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(id, orderMapper.toEntity(orderDto));
    }

    @DeleteMapping("/{id}")
    public Long deleteOrder(@PathVariable("id") Long orderId) {
        return orderService.deleteOrder(orderId);
    }

}
