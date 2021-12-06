package com.example.messagingapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.UserDto;
import com.example.messagingapp.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserDto userDto) {
        log.info("Rest API to save user {}", userDto);
        return ResponseEntity.ok(userService.save(userDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        log.info("Rest API to get user by id {}", id);
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping()
    public List<User> getAll() {
        log.info("Rest API to get all users");
        return userService.getAll();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        log.info("Rest API to delete user by id {}", id);
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
