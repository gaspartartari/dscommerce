package com.learning.dscommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dscommerce.DTO.UserDTO;
import com.learning.dscommerce.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> getMe(){
        UserDTO dto = userService.getMe();
        return ResponseEntity.ok(dto);
    }
    
}
