package com.learning.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscommerce.TokenUtil;
import com.learning.dscommerce.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {
    
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private TokenUtil tokenUtil;

    private String adminToken, clientToken, invalidToken;
    private String adminUsername, clientUsername;
    private String adminPassword, clientPassword;

    @BeforeEach
    void setup () throws Exception{

        adminUsername = "alex@gmail.com";
        adminPassword = "123456";
        clientUsername = "maria@gmail.com";
        clientPassword = "123456";

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "abcre"; // generates invalid token


    }


    // Busca de usuário retorna dados do usuário quando logado como admin
    @Test
    public void getMeShouldReturnUserDTOWhenAdminLogged() throws Exception{

        ResultActions result = mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + adminToken)
                .accept(MediaType.APPLICATION_JSON));
        
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.name").value("Alex Green"));
    }

    // Busca de usuário retorna dados do usuário quando logado como cliente
    @Test
    public void getMeShouldReturnUserDTOWhenClientLogged() throws Exception{

        ResultActions result = mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + clientToken)
                .accept(MediaType.APPLICATION_JSON));
        
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.name").value("Maria Brown"));
    }
    
    // Busca de usuário retorna 401 quando não logado como admin ou cliente
    @Test
    public void getMeShouldReturnUnauthorizedWhenNoUserLogged() throws Exception{

        ResultActions result = mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + invalidToken)
                .accept(MediaType.APPLICATION_JSON));
        
        result.andExpect(status().isUnauthorized());
    }
}
