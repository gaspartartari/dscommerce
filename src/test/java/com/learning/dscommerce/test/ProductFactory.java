package com.learning.dscommerce.test;

import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.DTO.ProductMinDTO;
import com.learning.dscommerce.entities.Product;

public class ProductFactory {
    
    public static Product createProduct(){
        return new Product(1L, "MacBook", "The best ever", 50.0, "www.imgrul.com");
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
