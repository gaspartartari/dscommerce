package com.learning.dscommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ProductDTO> findAll(Pageable pageable){
       Page<Product> result =  productRepository.findAll(pageable);
       return result.map(x -> new ProductDTO(x));
    }
}
