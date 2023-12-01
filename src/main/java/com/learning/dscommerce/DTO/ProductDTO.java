package com.learning.dscommerce.DTO;

import org.springframework.beans.BeanUtils;

import com.learning.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {
    
    private Long id;

    @NotBlank(message = "Product name canot be blank")
    @Size(min = 3, max = 80, message = "Product name has to have between 3 and 80 characters")
    private String name;

    @NotBlank(message = "Product description canot be blank")
    @Size(min = 10, message = "Description has to have at least 10 characters")
    private String description;

    @Positive(message = "Product price must be a positive value")
    private Double price;
    private String imgUrl;

    public ProductDTO(){
        
    }

    public ProductDTO(Product product) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductDTO other = (ProductDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
