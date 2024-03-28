package com.learning.dscommerce.test;

import java.util.List;

import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.DTO.ProductMinDTO;
import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.entities.Product;

public class ProductFactory {
    
    public static Product createProduct(){
        Product product = new Product(1L, "MacBook", "The best ever", 50.0, "www.imgrul.com");
        Category category = CategoryFactory.createCategory();
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(Long id, String name, String description, Double value, String imgUrl){
        return new Product(id, name, description, value, imgUrl);
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImgUrl(product.getImgUrl());
        productDTO.setPrice(product.getPrice());
        CategoryDTO catDTO = CategoryFactory.createCategoryDto();
        productDTO.addCategories(List.of(catDTO));
        return productDTO;
    }

    public static ProductDTO createProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImgUrl(product.getImgUrl());
        productDTO.setPrice(product.getPrice());
        CategoryDTO catDTO = CategoryFactory.createCategoryDto();
        productDTO.addCategories(List.of(catDTO));
        return productDTO;
    }

    public static ProductMinDTO createProductMinDTO(){
        Product product = createProduct();
        ProductMinDTO productMinDTO = new ProductMinDTO();
        productMinDTO.setId(product.getId());
        productMinDTO.setName(product.getName());
        productMinDTO.setImgUrl(product.getImgUrl());
        productMinDTO.setPrice(product.getPrice());
        return productMinDTO;
    }
}
