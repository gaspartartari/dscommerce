package com.learning.dscommerce.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.learning.dscommerce.entities.Role;

public class UserDTO {

    private Long id; 
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private List<String> authorities = new ArrayList<>();

    public UserDTO(){

    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void addAuthorities(List<Role> roles){
        for(GrantedAuthority role: roles){
            authorities.add(role.getAuthority());
        }
    }
    
}

