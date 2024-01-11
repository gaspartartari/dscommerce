package com.learning.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityMapperService entityMapperService;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
       List<Category> result =  categoryRepository.findAll();
       return entityMapperService.categoryToCategoryDTO(result);
    }
}
