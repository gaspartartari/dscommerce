package com.learning.dscommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.learning.dscommerce.DTO.CategoryDTO;
import com.learning.dscommerce.entities.Category;
import com.learning.dscommerce.repositories.CategoryRepository;
import com.learning.dscommerce.test.CategoryFactory;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
    
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EntityMapperService entityMapperService;

    private Category category;
    private List<Category> list;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setup() throws Exception{

        category = CategoryFactory.createCategory();
        list = new ArrayList<>();
        list.add(category);
        categoryDTO = CategoryFactory.createCategoryDto();

        Mockito.when(categoryRepository.findAll()).thenReturn(list);
        Mockito.when(entityMapperService.categoryToCategoryDTO(category)).thenReturn(categoryDTO);
    }

    @Test
    public void findAllShouldReturnListOfCategoryDTO(){

        List<CategoryDTO> result = categoryService.findAll();

        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).getId(), 1);
        Assertions.assertEquals(result.get(0).getName(), "Books");
        Mockito.verify(categoryRepository).findAll();

    }
}
