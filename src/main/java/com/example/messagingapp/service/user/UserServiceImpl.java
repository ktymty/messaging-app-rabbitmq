package com.example.messagingapp.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.UserDto;
import com.example.messagingapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(UserDto userDto) {
        User user = User.builder().name(userDto.getNickName()).build();
        if (user.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new user should not have an id");
        }
        user = userRepository.save(user);
        log.info("Saved user {}", user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        log.info("Get user by id {}", id);
        return userRepository
                .findById(id)
                .orElseThrow(() -> userNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        log.info("Get all users");
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id))
            throw userNotFoundException(id);
        userRepository.deleteById(id);
        log.info("User successfully deleted fo the id {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByName(String name) {
        log.info("Get user by name {}", name);
        return userRepository.findByName(name);
    }

    private ResponseStatusException userNotFoundException(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for the id " + id);
    }
}
