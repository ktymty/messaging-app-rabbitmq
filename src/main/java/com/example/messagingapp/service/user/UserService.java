package com.example.messagingapp.service.user;

import java.util.List;

import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.UserDto;

public interface UserService {

    User save(UserDto userDto);

    User getById(Long id);

    List<User> getAll();

    void deleteById(Long id);

    User findByName(String name);
}
