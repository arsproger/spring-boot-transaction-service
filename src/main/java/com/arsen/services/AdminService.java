package com.arsen.services;

import com.arsen.models.Order;
import com.arsen.models.Product;
import com.arsen.models.User;
import com.arsen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AdminService {
    private final UserService userService;
    private final ProductService productService;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(UserService userService, ProductService productService,
                        UserRepository userRepository) {
        this.userService = userService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public Boolean replenishment(Long userId, BigDecimal amount) {
        User user = userService.getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        return true;
    }

    public Boolean reserving(Long userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        if (user.getBalance().compareTo(product.getPrice()) < 0)
            return false;

        user.setBalance(user.getBalance().subtract(product.getPrice()));
        user.setReserveBalance(user.getReserveBalance().add(product.getPrice()));
        Order.builder()
                .date(LocalDateTime.now())
                .user(user)
                .product(product)
                .build();
        userRepository.save(user);
        return true;
    }

}
