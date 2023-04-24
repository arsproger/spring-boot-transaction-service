package com.arsen.services;

import com.arsen.enums.Role;
import com.arsen.models.User;
import com.arsen.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setRole(Role.ROLE_USER);
        user.setReserveBalance(new BigDecimal(0));
        if (user.getBalance() == null)
            user.setBalance(new BigDecimal(0));

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }

}
