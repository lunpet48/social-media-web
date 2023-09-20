package com.webapp.socialmedia.controller;

import com.webapp.socialmedia.entity.Role;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    IUserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        User u = new User();
        u.setRole(Role.ADMIN);
        u.setEmail("a");
        u.setPassword("a");
        userService.save(u);

        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
}
