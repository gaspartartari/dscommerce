package com.learning.dscommerce.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import com.learning.dscommerce.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    EntityMapperService entityMapperService;

    @Mock
    CustomUserUtil customUserUtil;

    private User user;
    private UserDTO userDTO;
    private List<UserDetailsProjection> userDetailsProjection;
    private String existingUsername, nonExsistingUsername;

    @BeforeEach
    void setup() throws Exception {

        user = UserFactory.createUserClient();
        existingUsername = "maria@gmail.com";
        nonExsistingUsername = "nonexisting@gmail.com";
        userDTO = UserFactory.createUserAdminDTO(user);
        userDetailsProjection = UserDetailsFactory.createCustomUserAdmin(existingUsername);

        Mockito.when(userRepository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetailsProjection);
        Mockito.when(userRepository.searchUserAndRolesByEmail(nonExsistingUsername)).thenReturn(new ArrayList<>());


        Mockito.when(userRepository.findByEmail(existingUsername)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail(nonExsistingUsername)).thenReturn(Optional.empty());
    }

    @Test
    public void getMeShouldThrowUsernameNotFoundException(){
        
        UserService spyService = Mockito.spy(userService);
        Mockito.doThrow(UsernameNotFoundException.class).when(spyService).authenticated();
        Mockito.when(entityMapperService.userToUserDto(user)).thenReturn(userDTO);

        Assertions.assertThrows(UsernameNotFoundException.class, () ->{
            spyService.getMe();
        });
    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserLogged(){
        
        UserService spyService = Mockito.spy(userService);
        Mockito.doReturn(user).when(spyService).authenticated();
        Mockito.when(entityMapperService.userToUserDto(user)).thenReturn(userDTO);

        UserDTO result = spyService.getMe();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), userDTO.getId());
        Assertions.assertEquals(result.getEmail(), userDTO.getEmail());
        Assertions.assertEquals(result.getName(), userDTO.getName());
        Assertions.assertEquals(result.getPhone(), userDTO.getPhone());
        Assertions.assertEquals(result.getAuthorities().get(0),"ROLE_CLIENT");
       
    }

    @Test
    public void authenticatedShouldReturnThrowUserNotFoundExceptionWhenUserDoesNotExist(){
        
        Mockito.when(customUserUtil.getLoggedUser()).thenReturn(nonExsistingUsername);

        Assertions.assertThrows(UsernameNotFoundException.class, () ->{
            User result = userService.authenticated();
        });

        Mockito.verify(userRepository).findByEmail(nonExsistingUsername);
    }

    @Test
    public void authenticatedShouldReturnUserIfUsernameExists() {

        Mockito.when(customUserUtil.getLoggedUser()).thenReturn(existingUsername);

        User result = userService.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), user.getId());
        Assertions.assertEquals(result.getName(), user.getName());
        Assertions.assertEquals(result.getPassword(), user.getPassword());
        Mockito.verify(userRepository).findByEmail(existingUsername);
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists() {

        UserDetails result = userService.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), "maria@gmail.com");
        Assertions.assertEquals(result.getPassword(), "12345");

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        authorities.forEach(auth -> System.out.println(auth.getAuthority()));
        Assertions.assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails result = userService.loadUserByUsername(nonExsistingUsername);
        });

        Mockito.verify(userRepository).searchUserAndRolesByEmail(nonExsistingUsername);
    }
}