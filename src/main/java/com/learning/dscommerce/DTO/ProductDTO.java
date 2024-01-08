package com.learning.dscommerce.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "There must be at least one category")
    private List<CategoryDTO> categories = new ArrayList<>();

    

    public ProductDTO (Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product product) {
        BeanUtils.copyProperties(product, this);
        for(Category category : product.getCategories()){
            categories.add(new CategoryDTO(category));
        }
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

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
        
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    
}
