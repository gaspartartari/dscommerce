package com.learning.dscommerce.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product result = productRepository.findById(id).get();
        ProductDTO dto = new ProductDTO(result);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable){
       Page<Product> result =  productRepository.findAll(pageable);
       return result.map(x -> new ProductDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        BeanUtils.copyProperties(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }
}
