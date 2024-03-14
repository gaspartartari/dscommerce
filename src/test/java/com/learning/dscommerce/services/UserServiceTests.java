package com.learning.dscommerce.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learning.dscommerce.DTO.UserDTO;
import com.learning.dscommerce.entities.User;
import com.learning.dscommerce.projections.UserDetailsProjection;
import com.learning.dscommerce.repositories.UserRepository;
import com.learning.dscommerce.test.UserDetailsFactory;
import com.learning.dscommerce.test.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTests{


    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock 
    EntityMapperService entityMapperService;


    private User user;
    private UserDTO userDTO;
    private List<UserDetailsProjection> userDetailsProjection;
    private String existingUsername, nonExsistingUsername;

    

    @BeforeEach
    void setup() throws Exception{

        user = UserFactory.createUserClient();
        existingUsername = "maria@gmail.com";
        nonExsistingUsername = "nonexisting@gmail.com";
        userDetailsProjection = UserDetailsFactory.createCustomUserAdmin(existingUsername);
        
        
        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetailsProjection);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExsistingUsername)).thenReturn(new ArrayList<>());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists(){

        UserDetails result = userService.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), "maria@gmail.com");
        Assertions.assertEquals(result.getPassword(), "12345");
        
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        authorities.forEach(auth -> System.out.println(auth.getAuthority()));
        Assertions.assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist(){

        Assertions.assertThrows(UsernameNotFoundException.class, () ->{
            UserDetails result = userService.loadUserByUsername(nonExsistingUsername);
        });

        Mockito.verify(userRepository).searchUserAndRolesByEmail(nonExsistingUsername);
    }
}