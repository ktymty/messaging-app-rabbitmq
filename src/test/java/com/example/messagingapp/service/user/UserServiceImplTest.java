package com.example.messagingapp.service.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.UserDto;
import com.example.messagingapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).name("tonyStark").build();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    @DisplayName("should save a given user")
    void save() {
        when(userRepository.save(any())).thenReturn(user);

        userService.save(UserDto.builder().nickName("tonyStark").build());

        verify(userRepository, times(1)).save(any());
    }
}