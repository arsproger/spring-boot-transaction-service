package com.arsen.services;

import com.arsen.enums.OrderStatus;
import com.arsen.models.Order;
import com.arsen.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Long createOrder(Order order) {
        return orderRepository.save(order).getId();
    }

    public Long deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return orderId;
    }

    public Long updateOrder(Long orderId, Order updatedOrder) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null)
            return null;

        order.setStatus(updatedOrder.getStatus());
        order.setUser(updatedOrder.getUser());
        order.setProduct(updatedOrder.getProduct());

        return orderRepository.save(order).getId();
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByDateBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByDateBetween(start, end);
    }

    public Long orderSumByProductId(Long productId) {
        return orderRepository.findByProductId(productId)
                .stream().filter(e -> e.getStatus().equals(OrderStatus.ACCEPTED))
                .mapToLong(e -> e.getProduct().getPrice().longValue()).sum();
    }

}
