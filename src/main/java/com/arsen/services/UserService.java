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
        if (user.getBalance() == null) user.setBalance(new BigDecimal(0));
        if (user.getReserveBalance() == null) user.setReserveBalance(new BigDecimal(0));

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null)
            return null;

        user.setFullName(updatedUser.getFullName());
        user.setBalance(updatedUser.getBalance());
        user.setReserveBalance(updatedUser.getReserveBalance());

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
