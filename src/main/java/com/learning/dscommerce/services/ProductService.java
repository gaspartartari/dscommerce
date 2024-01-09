package com.learning.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.DTO.ProductDTO;
import com.learning.dscommerce.DTO.ProductMinDTO;
import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.entities.Product;
import com.learning.dscommerce.repositories.ProductRepository;
import com.learning.dscommerce.services.exceptions.DatabaseException;
import com.learning.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityMapperService entityMapperService;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Product result = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
        ProductDTO dto = entityMapperService.productToProductDto(result);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable){
       Page<Product> result =  productRepository.searchByName(name, pageable);
       return result.map(x -> entityMapperService.productToProductMinDto(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return entityMapperService.productToProductDto(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return entityMapperService.productToProductDto(entity);
        } 
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteById(Long id){
        if(!productRepository.existsById(id))
            throw new ResourceNotFoundException("Resource not found: " + id);
        try {
            productRepository.deleteById(id);
        } 
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity constraint violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.getCategories().clear();
        for (CategoryDTO catDto: dto.getCategories()){
            Category category = new Category();
            category.setId(catDto.getId());;
            entity.getCategories().add(category);
        }
    }
}
