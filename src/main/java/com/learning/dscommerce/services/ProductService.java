package com.learning.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public ProductDTO findById(Long id){
        Product result = productRepository.findById(id).get();
        ProductDTO dto = new ProductDTO(result);
        return dto;
    }
}
