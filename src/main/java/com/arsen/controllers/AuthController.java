package com.arsen.controllers;

import com.arsen.dtos.UserDto;
import com.arsen.mappers.UserMapper;
import com.arsen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public Long createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userMapper.toEntity(userDto));
    }

}
