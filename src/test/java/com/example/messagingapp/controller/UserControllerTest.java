package com.example.messagingapp.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.messagingapp.model.User;
import com.example.messagingapp.model.dto.UserDto;
import com.example.messagingapp.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@DisplayName("User Controller")
class UserControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().nickName("tonyStark").build();
        user = User.builder().id(1L).name("tonyStark").build();
    }

    @AfterEach
    void tearDown() {
        userDto = null;
        user = null;
    }

    @Test
    @DisplayName("should save a give nickname")
    void save() throws Exception {
        when(userService.save(userDto)).thenReturn(user);
        this.mockMvc.perform(
                post("/v1/users").contentType(MediaType.APPLICATION_JSON).content("{\"nickName\":\"tonyStark\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1, 'name':'tonyStark'}"));
    }

    @Test
    @DisplayName("should return a user when given an id")
    void getById() throws Exception {
        when(userService.getById(1L)).thenReturn(user);
        this.mockMvc.perform(get("/v1/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{'id':1, 'name':'tonyStark'}"));
    }

    @Test
    @DisplayName("should return all users")
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(Arrays.asList(user, User.builder().id(2L).name("loki").build()));
        this.mockMvc.perform(get("/v1/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"tonyStark\"},{\"id\":2,\"name\":\"loki\"}]"));
    }

    @Test
    @DisplayName("should delete a user given an id")
    void deleteById() throws Exception {
        doNothing().when(userService).deleteById(1L);
        this.mockMvc.perform(delete("/v1/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}