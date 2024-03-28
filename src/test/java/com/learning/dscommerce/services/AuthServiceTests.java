package com.learning.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learning.dscommerce.entities.User;
import com.learning.dscommerce.services.exceptions.ForbiddenException;
import com.learning.dscommerce.test.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {
    
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    private User selfClient, admin, otherClient;

    @BeforeEach
    void setup() throws Exception{

        selfClient = UserFactory.createUserClient(1L, "Maria");
        admin = UserFactory.createUserAdmin();
        otherClient = UserFactory.createUserClient(5L, "Bob");
  
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenAdminLogged(){

        Long id = admin.getId();
        Mockito.when(userService.authenticated()).thenReturn(admin);

        Assertions.assertDoesNotThrow (() ->{
            authService.validateSelfOrAdmin(id);
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfClientLogged(){

        Long id = selfClient.getId();
        Mockito.when(userService.authenticated()).thenReturn(selfClient);

        Assertions.assertDoesNotThrow (() ->{
            authService.validateSelfOrAdmin(id);
        });
    }

    @Test
    public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenOtherClientLogged(){
        Long id = otherClient.getId();
        
        Mockito.when(userService.authenticated()).thenReturn(selfClient);

        Assertions.assertThrows(ForbiddenException.class, () ->{
            authService.validateSelfOrAdmin(id);
        });
    }
}
