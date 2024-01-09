package com.learning.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.dscommerce.entities.User;
import com.learning.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

    @Autowired
    private UserService userService;
    
    public void validateSelfOrAdmin (Long userId){
        User user = userService.authenticated();
        if (!user.hasRole("ROLE_ADMIN") && !user.getId().equals(userId))
            throw new ForbiddenException("Access deined");
    }
}
