package com.learning.dscommerce.DTO;

import org.springframework.beans.BeanUtils;

import com.learning.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductMinDTO {
    
    private Long id;

    
    private String name;
    private Double price;
    private String imgUrl;

    public ProductMinDTO(){
        
    }

    public ProductMinDTO(Product product) {
        BeanUtils.copyProperties(product, this);
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
