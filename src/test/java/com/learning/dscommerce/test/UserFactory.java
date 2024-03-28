package com.learning.dscommerce.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import com.learning.dscommerce.DTO.UserDTO;
import com.learning.dscommerce.entities.Role;
import com.learning.dscommerce.entities.User;

public class UserFactory {
    
    public static User createUserClient(){

        User user = new User(100L, "Maria", "maria@gmail.com", "111111", LocalDate.of(2024, 3, 24), "12345");
        user.addRole(new Role(1L, "ROLE_CLIENT"));;
        return user;
    }

    public static User createUserClient(Long id, String name){

        User user = new User(id, name, "maria@gmail.com", "111111", LocalDate.of(2024, 3, 24), "12345");
        user.addRole(new Role(1L, "ROLE_CLIENT"));;
        return user;
    }

    public static User createUserAdmin() {
        User user = new User(100L, "Maria", "maria@gmail.com", "111111", LocalDate.of(2024, 3, 24), "12345");
        user.addRole(new Role(1L, "ROLE_ADMIN"));;
        return user;
    }

    public static User createUserAdmin(Long id, String name){

        User user = new User(id, name, "maria@gmail.com", "111111", LocalDate.of(2024, 3, 24), "12345");
        user.addRole(new Role(1L, "ROLE_CLIENT"));;
        return user;
    }

    public static User createCustomUserClient(Long id, String username){

        User user = new User(id, "Maria", username, "111111", LocalDate.of(2024, 3, 24), "12345");
        user.addRole(new Role(1L, "ROLE_CLIENT"));;
        return user;
    }

    public static User createCustomUserAdmin(Long id, String username){

        User user = new User(id, "Maria", username, "111111", LocalDate.of(2024, 3, 24), "12345");
        user.addRole(new Role(1L, "ROLE_ADMIN"));;
        return user;
    }

    public static UserDTO createUserAdminDTO(User user) {
        UserDTO result = new UserDTO();
        List<Role> authorities = new ArrayList<>();
        BeanUtils.copyProperties(user, result);
        for(Role r : user.getRoles()){
            authorities.add(r);
        }
        result.addAuthorities(authorities);
        return result;
    }

}
