package com.arsen.repositories;

import com.arsen.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findByUserId(Long userId);
}
