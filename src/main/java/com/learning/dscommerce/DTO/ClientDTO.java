package com.learning.dscommerce.DTO;

public class ClientDTO {
    private Long id;
    private String name;

    public ClientDTO(){

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
