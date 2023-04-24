package com.arsen.controllers;

import com.arsen.dtos.UserDto;
import com.arsen.mappers.UserMapper;
import com.arsen.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.toDto(userService.getUserById(id));
    }

    @GetMapping("/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userMapper.toDto(userService.getUserByEmail(email));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return userMapper.toDto(userService.createUser(userMapper.toEntity(userDto)));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userMapper.toDto(userService.updateUser(id, userMapper.toEntity(userDto)));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
