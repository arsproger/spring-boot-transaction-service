package com.arsen.services;

import com.arsen.enums.Role;
import com.arsen.models.User;
import com.arsen.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CsvDownload csvDownload;
    private final OrderService orderService;

    public UserService(UserRepository userRepository, CsvDownload csvDownload, OrderService orderService) {
        this.userRepository = userRepository;
        this.csvDownload = csvDownload;
        this.orderService = orderService;
    }

    public Long createUser(User user) {
        user.setRole(Role.ROLE_USER);
        if (user.getBalance() == null) user.setBalance(BigDecimal.ZERO);
        if (user.getReserveBalance() == null) user.setReserveBalance(BigDecimal.ZERO);

        return userRepository.save(user).getId();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Long updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;

        user.setFullName(updatedUser.getFullName());
        user.setBalance(updatedUser.getBalance());
        user.setReserveBalance(updatedUser.getReserveBalance());

        return userRepository.save(user).getId();
    }

    public Long deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return userId;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }


    // метод получения списка транзакций пользователя
    public ResponseEntity<byte[]> downloadUserOrdersInfo(Long userId) {
        return csvDownload.download(orderService.getOrdersByUserId(userId));
    }

}
