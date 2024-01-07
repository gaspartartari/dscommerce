package com.learning.dscommerce.DTO;

import org.springframework.beans.BeanUtils;

import com.learning.dscommerce.entities.User;

public class ClientDTO {
    private Long id;
    private String name;
    
    public ClientDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ClientDTO(User entity) {
        BeanUtils.copyProperties(entity, this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
